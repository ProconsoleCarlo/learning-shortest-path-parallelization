# Plan: Reimplementazione degli algoritmi di shortest path con parallelizzazione ottimale

Creare una nuova implementazione da zero in un package separato (`v2`) con strutture dati ottimali, algoritmi paralleli corretti (Delta-Stepping per Dijkstra, double-buffering per Bellman-Ford), e un'architettura pulita che supporti sia grafi densi (matrix) che sparsi (adjacency list).

## Steps

### 1. Creare il package `it.proconsole.learning.shortestpath.parallelization.v2.graph` con l'interfaccia `Graph` generica e implementare sia `CompactAdjacencyListGraph` che `MatrixGraph` con la stessa interfaccia

**Obiettivo**: Definire le strutture dati per i grafi con interfaccia comune, implementando entrambe le rappresentazioni per supportare sia grafi sparsi che densi.

**Dettagli**:
- Creare l'interfaccia `Graph` con metodi:
  - `int vertexCount()`
  - `Iterable<Edge> getEdges(int vertex)` - per iterare sugli archi uscenti
  - `int getEdgeWeight(int from, int to)` - per accesso diretto al peso (utile per Matrix)
  - `boolean hasEdge(int from, int to)` - per verificare esistenza arco
  - `boolean hasNegativeEdges()`
  - `int edgeCount()` - numero totale di archi
  
- Implementare `CompactAdjacencyListGraph`:
  - Usare array primitivi per efficienza memoria
  - Struttura CSR (Compressed Sparse Row): `int[] offsets` (indici di inizio per ogni vertice) + `int[] destinations` + `int[] weights`
  - Ottimale per grafi sparsi (O(V + E) memoria)
  - Accesso cache-friendly durante iterazione
  - Immutabile dopo costruzione per thread-safety
  - `getEdges(vertex)` ritorna iteratore sugli archi del vertice
  - `getEdgeWeight(from, to)` richiede ricerca lineare negli archi del vertice from (O(degree))
  
- Implementare `MatrixGraph`:
  - Array 2D `int[][] adjacencyMatrix` con convenzione: `0` = nessun arco, `value` = peso arco
  - Ottimale per grafi densi (densità > 0.5)
  - Accesso O(1) per `getEdgeWeight(from, to)` e `hasEdge(from, to)`
  - `getEdges(vertex)` richiede scansione della riga (O(V)) ma accettabile per grafi densi
  - Memoria O(V²) - limitare uso a grafi con V < 10000 o densità molto alta
  
- **Compatibilità interfaccia**:
  - Entrambe le implementazioni rispettano pienamente l'interfaccia `Graph`
  - Trade-off accettato: 
    - CompactAdjacencyListGraph: `getEdges()` veloce O(degree), `getEdgeWeight()` lento O(degree)
    - MatrixGraph: `getEdgeWeight()` veloce O(1), `getEdges()` moderato O(V)
  - Gli algoritmi devono usare il metodo più efficiente per la propria logica:
    - Dijkstra/Bellman-Ford usano principalmente `getEdges()` → favorisce CompactAdjacencyListGraph
    - Matrix utile per confronti e analisi teoriche

**File da creare**:
- `v2/graph/Graph.java`
- `v2/graph/Edge.java` (record immutabile: `destination`, `weight`)
- `v2/graph/CompactAdjacencyListGraph.java`
- `v2/graph/MatrixGraph.java`
- `v2/graph/GraphBuilder.java` (builder per costruzione incrementale → conversione a immutabile CSR/Matrix)

---

### 2. Implementare `it.proconsole.learning.shortestpath.parallelization.v2.algorithm.DijkstraDeltaStepping` usando ForkJoinPool con bucket per livelli di distanza (parametro Δ configurabile), AtomicIntegerArray per aggiornamenti thread-safe, e task ricorsivi per processare bucket in parallelo

**Obiettivo**: Implementare l'algoritmo Delta-Stepping per Dijkstra parallelo con scalabilità ottimale.

**Dettagli**:
- **Algoritmo Delta-Stepping**:
  - Organizzare i nodi in bucket basati su intervalli di distanza [iΔ, (i+1)Δ)
  - Parametro Δ configurabile (euristica: max_weight / num_threads)
  - Processare bucket in parallelo con ForkJoinPool
  
- **Strutture dati thread-safe**:
  - `AtomicIntegerArray distances` per distanze minime
  - `ConcurrentLinkedQueue<Integer>[] buckets` per i bucket
  - `AtomicInteger currentBucket` per sincronizzazione
  
- **Algoritmo**:
  1. Light edges (peso ≤ Δ): processare all'interno dello stesso bucket
  2. Heavy edges (peso > Δ): inserire nodi nei bucket futuri
  3. ForkJoinTask ricorsivi per partizionare il lavoro all'interno di ogni bucket
  
- **Ottimizzazioni**:
  - Chunking per ridurre overhead di task creation
  - Local relaxation buffer per ridurre contention
  - Terminazione anticipata quando bucket corrente è vuoto

**File da creare**:
- `v2/algorithm/ShortestPathAlgorithm.java` (interfaccia comune)
- `v2/algorithm/DijkstraDeltaStepping.java`
- `v2/algorithm/DijkstraDeltaSteppingTask.java` (ForkJoinTask interno)
- `v2/result/ShortestPathResult.java` (risultato con distanze e predecessori)

---

### 3. Implementare `it.proconsole.learning.shortestpath.parallelization.v2.algorithm.DijkstraSerialOptimized` con binary heap efficiente come baseline e fallback, garantendo la stessa interfaccia `ShortestPathAlgorithm` del Delta-Stepping

**Obiettivo**: Fornire implementazione seriale ottimizzata per confronto e fallback automatico per grafi piccoli.

**Dettagli**:
- **Binary Heap custom**:
  - Array-based min-heap con indicizzazione diretta per decrease-key O(log n)
  - `int[] heap` - heap array
  - `int[] position` - posizione di ogni nodo nell'heap
  - Operazioni: `insert`, `extractMin`, `decreaseKey`
  
- **Algoritmo classico**:
  - Inizializzare distanze a infinito, source a 0
  - Estrarre nodo con distanza minima
  - Rilassare archi uscenti
  - Continuare fino a heap vuoto
  
- **Fallback automatico**:
  - Se `vertices < PARALLEL_THRESHOLD` (es. 10000), usare versione seriale
  - Evitare overhead di parallelizzazione su grafi piccoli

**File da creare**:
- `v2/algorithm/DijkstraSerialOptimized.java`
- `v2/datastructure/BinaryMinHeap.java`
- `v2/algorithm/AlgorithmSelector.java` (selettore automatico serial/parallel)

---

### 4. Implementare `it.proconsole.learning.shortestpath.parallelization.v2.algorithm.BellmanFordParallelDoubleBuffer` usando due array di distanze (current/next) per evitare race condition, con ForkJoinPool per partizionare l'edge relaxation e sincronizzazione tra iterazioni

**Obiettivo**: Implementare Bellman-Ford parallelo corretto con double buffering per evitare data races.

**Dettagli**:
- **Double Buffering**:
  - `int[] currentDistances` - distanze dell'iterazione corrente
  - `int[] nextDistances` - distanze da aggiornare
  - Swap dei buffer a fine di ogni iterazione
  
- **Parallelizzazione**:
  - Partizionare i vertici tra thread con ForkJoinPool
  - Ogni thread processa un subset di vertici
  - Edge relaxation: `nextDistances[v] = min(nextDistances[v], currentDistances[u] + weight(u,v))`
  
- **Sincronizzazione**:
  - Barrier (CountDownLatch o Phaser) tra iterazioni
  - V-1 iterazioni totali
  - Check per convergenza anticipata (se nessun cambiamento in un'iterazione)
  
- **Ottimizzazioni**:
  - AtomicInteger per aggiornamenti a nextDistances se necessario
  - Chunking per bilanciare carico
  - Early termination se nessun aggiornamento

**File da creare**:
- `v2/algorithm/BellmanFordParallelDoubleBuffer.java`
- `v2/algorithm/BellmanFordSerial.java` (baseline)
- `v2/algorithm/BellmanFordRelaxationTask.java` (ForkJoinTask)

---

### 5. Creare `it.proconsole.learning.shortestpath.parallelization.v2.benchmark.BenchmarkRunner` per eseguire confronti sistematici tra algoritmi seriali/paralleli su grafi di dimensioni e densità variabili, con statistiche dettagliate (tempo, speedup, efficienza)

**Obiettivo**: Sistema completo di benchmarking per valutare performance e correttezza.

**Dettagli**:
- **Configurazione benchmark**:
  - Parametri: vertices (range: 100-100000), density (range: 0.1-0.9), warmup iterations, measurement iterations
  - Graph types: AdjacencyList, Matrix (se implementato)
  - Algoritmi da testare: tutti serial + parallel
  
- **Metriche**:
  - Tempo medio di esecuzione (ms)
  - Speedup: T_serial / T_parallel
  - Efficienza: Speedup / num_cores
  - Throughput: edges_processed / second
  
- **Validazione correttezza**:
  - Confrontare risultati tra serial e parallel
  - Verificare distanze identiche
  - Report errori se discrepanze
  
- **Output**:
  - Console logging con tabelle formattate
  - Export CSV per analisi esterna
  - Grafici opzionali (con libreria charting)

**File da creare**:
- `v2/benchmark/BenchmarkRunner.java`
- `v2/benchmark/BenchmarkConfig.java`
- `v2/benchmark/BenchmarkResult.java`
- `v2/benchmark/ResultValidator.java`
- `v2/benchmark/CSVExporter.java`

---

### 6. Implementare `it.proconsole.learning.shortestpath.parallelization.v2.Main` come nuovo entry point che usa il `BenchmarkRunner` con parametri configurabili da riga di comando (vertices, density, graphType, delta, threads)

**Obiettivo**: Entry point pulito per eseguire i benchmark con configurazione flessibile.

**Dettagli**:
- **Parsing argomenti**:
  - `--vertices=N` (default: 10000)
  - `--density=D` (default: 0.3)
  - `--graphType=TYPE` (adjacency|matrix, default: adjacency)
  - `--delta=D` (per Delta-Stepping, default: auto)
  - `--threads=T` (default: Runtime.availableProcessors())
  - `--warmup=N` (default: 3)
  - `--iterations=N` (default: 10)
  - `--source=N` (default: 0)
  - `--export=file.csv` (opzionale)
  
- **Workflow**:
  1. Parse argomenti
  2. Configurare ForkJoinPool con numero thread specificato
  3. Generare grafo con parametri
  4. Eseguire warmup
  5. Eseguire benchmark con tutte le varianti
  6. Validare correttezza
  7. Stampare risultati + export CSV se richiesto
  
- **Help message**:
  - Descrizione chiara di ogni parametro
  - Esempi di utilizzo

**File da creare**:
- `v2/Main.java`
- `v2/config/CommandLineParser.java`
- `v2/graph/GraphGenerator.java` (generazione grafi random)

---

## Further Considerations

### 1. Parametro Delta

Per Delta-Stepping, il valore ottimale di Δ dipende dalla distribuzione dei pesi:
- **Euristica iniziale**: `Δ = max_weight / threads`
- **Regola generale**: 
  - Δ piccolo → più bucket, più parallelismo, ma più overhead
  - Δ grande → meno bucket, meno parallelismo, ma meno overhead
- **Soluzione**: Rendere Δ configurabile e fornire auto-tuning basato su:
  - Numero di thread disponibili
  - Range dei pesi nel grafo
  - Densità del grafo
- **Sperimentazione**: Includere nel benchmark test con valori diversi di Δ per identificare l'ottimo

### 2. Scelta struttura dati

Implementare entrambe le rappresentazioni con interfaccia `Graph` comune:

**CompactAdjacencyListGraph (CSR)**:
- ✅ Memoria: O(V + E) - ottimale per grafi sparsi
- ✅ Cache-friendly: accesso sequenziale durante iterazione archi
- ✅ Scalabilità: performance costanti per grafi grandi e sparsi
- ✅ Iterazione archi velocissima: O(degree)
- ❌ Accesso peso specifico: O(degree) - ricerca lineare
- ❌ Costruzione: richiede conoscenza a priori di tutti gli archi (immutabile)

**MatrixGraph**:
- ✅ Accesso O(1) a qualsiasi peso: `getEdgeWeight(from, to)`
- ✅ Verifica esistenza arco O(1): `hasEdge(from, to)`
- ✅ Semplice implementazione e debugging
- ✅ Ottimale per grafi densi (densità > 0.5)
- ❌ Memoria: O(V²) - proibitivo per grafi grandi e sparsi
- ❌ Iterazione archi: O(V) per scansionare riga (vs O(degree) per CSR)
- ❌ Cache: poor locality per grafi sparsi

**Strategia di utilizzo**:
- Implementare entrambe le strutture con la stessa interfaccia `Graph`
- Trade-off accettabili:
  - CompactAdjacencyListGraph privilegia `getEdges()` (usato da Dijkstra/Bellman-Ford)
  - MatrixGraph privilegia `getEdgeWeight()` (utile per analisi e verifiche)
- Factory pattern per selezione automatica basata su densità e dimensione:
  ```
  if (vertices < 10000 && density > 0.5) → MatrixGraph
  else → CompactAdjacencyListGraph
  ```
- Permettere override manuale della scelta per sperimentazione e benchmark
- Gli algoritmi sono progettati per funzionare efficientemente con entrambe (privilegiano iterazione archi)

**Benchmarking comparativo**:
- Includere test di performance per entrambe le rappresentazioni
- Misurare tempo costruzione, memoria utilizzata, tempo esecuzione algoritmi
- Documentare curve di performance in funzione di vertices e density

### 3. Verifica correttezza

Includere nel `BenchmarkRunner` verifiche automatiche:

1. **Consistenza risultati**:
   - Confrontare distanze tra serial e parallel per stesso grafo
   - Tolleranza zero: distanze devono essere identiche
   
2. **Proprietà matematiche**:
   - Distanza(source, source) = 0
   - Distanza(source, v) ≤ Distanza(source, u) + weight(u, v) (triangle inequality)
   - Path reconstruction: verificare che i predecessori formino un albero valido
   
3. **Test cases specifici**:
   - Grafi sconnessi
   - Grafi con cicli
   - Grafi con pesi eterogenei
   - Casi limite (1 nodo, nodi isolati)
   
4. **Performance regression**:
   - Tracciare performance nel tempo
   - Alert se degradazione > 10%

### 4. Considerazioni aggiuntive

**Thread pool configuration**:
- Usare ForkJoinPool custom con parallelismo configurabile
- Evitare common pool per controllare risorse
- Work stealing per bilanciamento automatico

**Memory management**:
- Preallocare array per evitare GC overhead durante benchmark
- Considerare off-heap memory per grafi molto grandi (usando Unsafe o ByteBuffer)

**Negative cycle detection** (Bellman-Ford):
- Aggiungere V-esima iterazione per detection
- Report se presente ciclo negativo

**Path reconstruction**:
- Mantenere array predecessori oltre alle distanze
- Metodo `getPath(target)` per ricostruire shortest path

**Testing**:
- Unit test per ogni componente
- Property-based testing con grafi random
- Benchmark test per non-regression

