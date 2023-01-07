# C implementation

This project was originally developed as "parallel programming project" for the university
course [Advanced computer architectures](http://www-4.unipv.it/offertaformativa/portale/corso.php?idAttivitaFormativa=372483&modulo=0&lingua=2)
, in the time between 22/12/2014 and 15/01/2015.

The purpose was to show a possible implementation in the C language of two algorithms that compute the shortest path in
a graph (Dijkstra and Bellman-Ford) and a possible parallelization of them using the Open MP model.

The documentation [Graph analysis shortest path](../docs/Graph%20analysis%20shortest%20path.pdf) explains the aim, the
used test hardware and the results we obtained.

It is written in C using [OpenMP](https://www.openmp.org/)

## Install

### With Maven

```sh
mvn clean install
```

### Without Maven

- On Windows Subsystem for Linux or Linux:
    ```sh
    cd ./src/main/c
    ./makefile.sh
    cd ../../..
    ```
- On Windows:
    ```sh
    cd ./src/main/c
    .\makefile.bat
    cd ../../..
    ```

## Execution

### With Maven

```sh
mvn compile exec:exec
```

### Without Maven

**After install** run the following command:

- on Windows Subsystem for Linux or Linux:
    ```sh
    ./src/main/c/ShortestPath
    ```
- on Windows:
    ```sh
    cd ./src/main/c
    .\ShortestPath.bat
    cd ../../..
    ```

## Authors

👤 **Carlo Bobba**

* Github: [@ProconsoleCarlo](https://github.com/ProconsoleCarlo)
* LinkedIn: [@carlo-bobba](https://linkedin.com/in/carlo-bobba)

👤 **Eleonora Aiello**

* LinkedIn: [@eleonora-aiello](https://www.linkedin.com/in/eleonora-aiello-8397a8196)

## Show your support

Give a ⭐️ if this project helped you!

***
_This README was generated with ❤️ by [readme-md-generator](https://github.com/kefranabg/readme-md-generator)_