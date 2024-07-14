package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.*;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private AutorRepository autorRepository;
    private LibroRepository libroRepository;
    public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar Libro Por Titulo
                    2 - Listar Libros Registrados
                    3 - Listar Autores Registrados
                    4 - Listar Autores Vivos En Un Determinado Año
                    5 - Listar Libros Por Idioma 
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    mostrarAutoresPorAnioVivo();
                    break;
                case 5:
                    mostrarLibroPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private void buscarLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        var nombreCompleto = "";
        nombreCompleto = nombreLibro.replace(" ", "%20");
        var json = consumoApi.obtenerDatos(URL_BASE+nombreCompleto);
        System.out.println(json);
        DatosLibro datos = conversor.obtenerDatos(json, DatosLibro.class);

        Optional<Libro> libroBuscado= libroRepository.findByTitulo(datos.resultados().get(0).titulo());
        if(libroBuscado.isPresent()){
            System.out.println("El libro ya fue registrado");
        }else{
            Libro libro = new Libro(datos.resultados().get(0).titulo(),datos.resultados().get(0).idiomas().get(0),datos.resultados().get(0).numeroDescargas());
            Autor autor = new Autor(datos.resultados().get(0).autores().get(0).nombre(),datos.resultados().get(0).autores().get(0).anioNacimiento(),datos.resultados().get(0).autores().get(0).anioFallecimiento());
            autorRepository.save(autor);
            System.out.println("Autor"+autor.getNombre());
            List<Autor> autorGuardar= autorRepository.findByNombre(autor.getNombre());
            if(!autorGuardar.isEmpty()){
                var autorAGuardar = autorGuardar.get(0);
                libro.setAutor(autorAGuardar);
                System.out.println(libro.toString());
                libroRepository.save(libro);
            }
        }
    }
    private void mostrarLibrosBuscados() {
        List<Libro> libros = libroRepository.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libro::getNumeroDescargas))
                .forEach(System.out::println);


    }private void mostrarAutores() {
        List<Autor> autores = autorRepository.findAll();
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(autor -> {
                    {
                        System.out.println("---------------");
                        System.out.println(autor.toString());
                        Libro libro = autor.getLibro();
                        if (libro != null) {
                            System.out.println("Libro: " + libro.getTitulo());
                            System.out.println("---------------");
                        } else {
                            System.out.println("  No tiene libro relacionado");
                        }
                    }
                });
    }

    private void mostrarAutoresPorAnioVivo() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar:");
        var anioAutor = teclado.nextInt();
        List<Autor> autores = autorRepository.buscarAutorPorAnioVivo(anioAutor);

        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(autor -> {
                    {
                        System.out.println("---------------");
                        System.out.println(autor.toString());
                        Libro libro = autor.getLibro();
                        if (libro != null) {
                            System.out.println("Libro: " + libro.getTitulo());
                            System.out.println("---------------");
                        } else {
                            System.out.println("  No tiene libro relacionado");
                        }
                    }
                });
    }
    private void mostrarLibroPorIdioma() {
        System.out.println("Escribe el idioma de búsqueda");
        var menu = """
                    en
                    fr
                    """;
        System.out.println(menu);
        var idioma = teclado.nextLine();
        List<Libro> libros = libroRepository.buscarLibroPorIdioma(idioma);

        libros.stream()
                .sorted(Comparator.comparing(Libro::getIdiomas))
                .forEach(System.out::println);
    }
}

