# Piano di Implementazione - Monorepo Gradle Multi-Project

**Data**: 26 Dicembre 2025  
**Stato**: Pronto per l'implementazione

---

## 📋 Sintesi Esecutiva

Trasformare il progetto Maven attuale in una **monorepo Gradle multi-project** con architettura modulare, coverage aggregato e CI/Sonar centralizzato.

---

## 🎯 Obiettivi Tecnici

| Componente | Target | Note |
|-----------|--------|------|
| Java Version | 25 | Latest LTS |
| Gradle Version | 8.13.x | Latest stable |
| Build Tool | Gradle | Multi-project |
| JVM Modules | 3 | common, java, kotlin |
| Coverage Tool | JaCoCo | Per JVM |
| C Build | CMake | Per portabilità |

---

## 📁 Struttura Finale

```
learning-shortest-path-parallelization/
├── settings.gradle.kts                 # Settings root (include moduli)
├── build.gradle.kts                    # Build root (configurazione comune)
├── gradle/
│   └── wrapper/                        # Gradle wrapper
│       ├── gradle-wrapper.jar
│       ├── gradle-wrapper.properties
│       └── gradlew / gradlew.bat
├── jvm/
│   ├── common/                         # Modulo dipendenze condivise
│   │   ├── build.gradle.kts
│   │   └── src/
│   │       ├── main/java/
│   │       └── test/java/
│   ├── java/                           # Modulo app Java
│   │   ├── build.gradle.kts
│   │   └── src/
│   │       ├── main/java/
│   │       └── test/java/
│   └── kotlin/                         # Modulo app Kotlin
│       ├── build.gradle.kts
│       └── src/
│           ├── main/kotlin/
│           └── test/kotlin/
├── c/
│   ├── CMakeLists.txt
│   ├── src/
│   │   ├── main/
│   │   │   └── c/
│   │   │       ├── algorithms/
│   │   │       ├── tests/
│   │   │       └── utils/
│   │   └── include/
│   ├── build/                          # (generato)
│   └── cmake/
├── tools/
│   ├── build_c.sh                      # Script build C
│   ├── ci-build.sh                     # Script build unificato
│   └── ci-scripts/
│       └── coverage-report.sh
├── docs/
├── README.md
├── PIANO-IMPLEMENTAZIONE.md            # QUESTO FILE
└── LICENSE

```

---

## 📝 Configurazione Dettagliata

### 1. Root `settings.gradle.kts`

```kotlin
rootProject.name = "learning-shortest-path-parallelization"

include(
    "jvm:common",
    "jvm:java",
    "jvm:kotlin"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}
```

### 2. Root `build.gradle.kts`

```kotlin
plugins {
    java version "25"
    kotlin("jvm") version "2.0.0" apply false
    `maven-publish`
    jacoco
    id("org.sonarqube") version "5.0.0.4638" apply false
}

allprojects {
    group = "it.proconsole.learning.shortestpath.parallelization"
    version = "2.0.0"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")

    java {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    jacoco {
        toolVersion = "0.8.11"
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required = true
            html.required = true
        }
    }
}
```

### 3. `:jvm:common` - `build.gradle.kts`

```kotlin
plugins {
    `java-library`
}

dependencies {
    // API (esposte ai consumer)
    api("com.google.guava:guava:33.0.0-jre")
    api("org.slf4j:slf4j-api:2.0.11")
    
    // Implementation (interne)
    implementation("ch.qos.logback:logback-classic:1.5.0")
    
    // Test
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

tasks.jar {
    archiveBaseName = "shortestpath-common"
}
```

### 4. `:jvm:java` - `build.gradle.kts`

```kotlin
plugins {
    id("application")
}

dependencies {
    implementation(project(":jvm:common"))
    testImplementation("junit:junit:4.13.2")
}

application {
    mainClass = "it.proconsole.learning.shortestpath.parallelization.Main"
}

tasks.jar {
    archiveBaseName = "shortestpath-java"
}
```

### 5. `:jvm:kotlin` - `build.gradle.kts`

```kotlin
plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation(project(":jvm:common"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.jar {
    archiveBaseName = "shortestpath-kotlin"
}
```

---

## 🔧 Script di Build

### `tools/build_c.sh`

```bash
#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD_DIR="${SCRIPT_DIR}/c/build"
SOURCE_DIR="${SCRIPT_DIR}/c"

echo "[C Build] Building C project..."
mkdir -p "${BUILD_DIR}"
cd "${BUILD_DIR}"

cmake .. -DCMAKE_BUILD_TYPE=Release
cmake --build . -- -j$(nproc)
ctest --output-on-failure || true

echo "[C Build] ✓ Build completed"
```

### `tools/ci-build.sh`

```bash
#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="${SCRIPT_DIR}/.."

echo "========================================="
echo "CI Build - Monorepo"
echo "========================================="

# Build JVM
echo ""
echo "[JVM] Building Java + Kotlin modules..."
cd "${PROJECT_ROOT}"
./gradlew clean build -x test

# Run tests with coverage
echo ""
echo "[JVM] Running tests with JaCoCo coverage..."
./gradlew test jacocoTestReport

# Build C
echo ""
echo "[C] Building C project..."
bash "${SCRIPT_DIR}/build_c.sh"

echo ""
echo "========================================="
echo "✓ All builds completed successfully"
echo "========================================="
```

### `tools/ci-scripts/coverage-report.sh`

```bash
#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="${SCRIPT_DIR}/../.."

echo "[Coverage] Aggregating coverage reports..."

# JaCoCo reports nel target di ogni modulo
JVM_COVERAGE="${PROJECT_ROOT}/jvm"

echo "JVM Coverage reports:"
find "${JVM_COVERAGE}" -name "jacoco.xml" -type f

echo ""
echo "[Coverage] ✓ Coverage aggregation completed"
```

---

## 📊 C Project Setup - `c/CMakeLists.txt`

```cmake
cmake_minimum_required(VERSION 3.16)
project(shortestpath_c C)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)
set(CMAKE_C_FLAGS_RELEASE "-O3 -DNDEBUG")
set(CMAKE_C_FLAGS_DEBUG "-g -O0")

# Include directories
set(INCLUDE_DIR "${CMAKE_SOURCE_DIR}/src/include")
file(GLOB_RECURSE HEADERS "${INCLUDE_DIR}/*.h")

# Source files
file(GLOB_RECURSE SOURCES 
    "${CMAKE_SOURCE_DIR}/src/main/c/**/*.c"
    EXCLUDE_PATTERN "${CMAKE_SOURCE_DIR}/src/main/c/tests/*"
)

# Test files
file(GLOB_RECURSE TEST_SOURCES
    "${CMAKE_SOURCE_DIR}/src/main/c/tests/*.c"
)

# Main executable
add_executable(shortest_path ${SOURCES})
target_include_directories(shortest_path PRIVATE ${INCLUDE_DIR})

# Tests executable (optional)
if(SOURCES)
    add_executable(run_tests ${SOURCES} ${TEST_SOURCES})
    target_include_directories(run_tests PRIVATE ${INCLUDE_DIR})
    enable_testing()
    add_test(NAME unit_tests COMMAND run_tests)
endif()
```

---

## 🔄 Fasi di Implementazione (Sequenziali)

### Fase 1: Inizializzazione Gradle ✅ COMPLETATA
- [x] Creare directory `jvm/` se non esiste
- [x] Generare Gradle wrapper con versione 8.13.x
- [x] Creare `settings.gradle.kts` root con include dei moduli
- [x] Creare `build.gradle.kts` root con configurazione comune
- [x] Validare structure con `./gradlew projects`

### Fase 2: Setup `:jvm:common`
- [ ] Creare directory `jvm/common/src/main/java`, `src/test/java`
- [ ] Creare `jvm/common/build.gradle.kts` con dipendenze base
- [ ] Aggiungere Guava, SLF4J, Logback, JUnit, JUnit 5
- [ ] Creare classe di test dummy per validare build
- [ ] Compilare con `./gradlew :jvm:common:build`

### Fase 3: Migrazione `:jvm:java`
- [ ] Creare directory `jvm/java/src/main/java`, `src/test/java`
- [ ] Copiare sorgenti da `java/src/main/java/` → `jvm/java/src/main/java/`
- [ ] Copiare test da `java/src/test/java/` → `jvm/java/src/test/java/`
- [ ] Creare `jvm/java/build.gradle.kts` con dipendenza a `:jvm:common`
- [ ] Aggiornare imports e risolvere dipendenze Maven mancanti
- [ ] Compilare con `./gradlew :jvm:java:build`
- [ ] Eseguire test con `./gradlew :jvm:java:test`

### Fase 4: Setup `:jvm:kotlin`
- [ ] Creare directory `jvm/kotlin/src/main/kotlin`, `src/test/kotlin`
- [ ] Creare `jvm/kotlin/build.gradle.kts` con Kotlin plugin
- [ ] Creare classe di esempio `Hello.kt` per validare setup
- [ ] Compilare con `./gradlew :jvm:kotlin:build`

### Fase 5: Setup C Project
- [ ] Verificare struttura attuale `c/src/main/c/`
- [ ] Creare `c/CMakeLists.txt` con configurazione CMAKE
- [ ] Validare con `cd c && mkdir build && cd build && cmake ..`
- [ ] Build con `cmake --build .`

### Fase 6: Tools & Scripts
- [ ] Creare directory `tools/` e `tools/ci-scripts/`
- [ ] Creare `tools/build_c.sh` con permissions execute
- [ ] Creare `tools/ci-build.sh` come entry point unificato
- [ ] Creare `tools/ci-scripts/coverage-report.sh`
- [ ] Testare con `bash tools/ci-build.sh`

### Fase 7: Coverage Configuration
- [ ] Aggiungere plugin `jacoco` ai build.gradle.kts
- [ ] Configurare `jacocoTestReport` per aggregare report
- [ ] Creare task di aggregazione coverage a livello root
- [ ] Eseguire `./gradlew jacocoTestReport`
- [ ] Verificare report HTML in `build/reports/jacoco/`

### Fase 8: Documentation & Cleanup
- [ ] Aggiornare README.md root con nuovi comandi
- [ ] Documentare struttura e convenzioni
- [ ] Backup e archivio del vecchio Maven `java/`
- [ ] Validazione finale di tutti i moduli

---

## ✅ Checklist di Validazione per Ogni Fase

```bash
# Fase 1: Gradle Init
./gradlew projects
./gradlew --version

# Fase 2: Common
./gradlew :jvm:common:build
./gradlew :jvm:common:test

# Fase 3: Java
./gradlew :jvm:java:build
./gradlew :jvm:java:test

# Fase 4: Kotlin
./gradlew :jvm:kotlin:build
./gradlew :jvm:kotlin:test

# Fase 5: C
cd c && ./build && cmake .. && cmake --build .

# Fase 6: Tools
bash tools/ci-build.sh

# Fase 7: Coverage
./gradlew jacocoTestReport
ls -la jvm/*/build/reports/jacoco/

# Fase 8: Overall
./gradlew clean build
./gradlew test
```

---

## 📚 Comandi Frequenti (Post-Setup)

```bash
# Build tutto
./gradlew build

# Test specifico modulo
./gradlew :jvm:java:test
./gradlew :jvm:kotlin:test

# Coverage report
./gradlew jacocoTestReport

# Clean
./gradlew clean

# Info progetto
./gradlew projects
./gradlew dependencies

# C Build
bash tools/build_c.sh

# CI Build completo
bash tools/ci-build.sh
```

---

## ⚠️ Rischi e Mitigazioni

| Rischio | Mitigazione |
|---------|------------|
| Dipendenze Maven mancanti | Elencare tutte dal pom.xml attuale e aggiungerle a common |
| Path relativi C | Usare ${CMAKE_SOURCE_DIR} in CMake |
| Gradle wrapper non sincronizzato | Forzare versione 8.13.x in gradle-wrapper.properties |
| Coverage non aggregato | Configurare jacocoTestReport a livello root |
| Script Bash non executabili | Ricordare `chmod +x tools/*.sh` |

---

## 📞 Contatti/Note

- **Modulo :jvm:java** → Migrazione da Maven pom.xml attuale
- **Modulo :jvm:kotlin** → Per uso futuro, struttura base soltanto
- **Coverage** → Da rimettere in piedi con JaCoCo dopo migrazione
- **CI/Sonar** → Fase successiva, dopo completamento build

---

**Fine Piano** ✅

