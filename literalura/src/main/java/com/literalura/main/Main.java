package com.literalura.main;


import com.literalura.entity.AuthorEntity;
import com.literalura.entity.BookEntity;
import com.literalura.model.Book;
import com.literalura.model.Result;
import com.literalura.repository.AuthorRepository;
import com.literalura.repository.BookRepository;
import com.literalura.service.APIConsumer;
import com.literalura.service.DataConverter;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private APIConsumer APIConsumer = new APIConsumer();
    private DataConverter converter = new DataConverter();
    private Scanner scanner = new Scanner(System.in);

    private BookRepository bookRepository;

    private AuthorRepository authorRepository;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void menu() {

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Lista de libros buscados
                    3-  Lista de Autores
                    4-  Lista de autores vivos en determinado año
                    5-  Listar libros en un idioma
                    6-  Listar el top 10 de los libros mas buscados
                    7-  Listar el top 10 de los libros mas descargados
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    searchedBookList();
                    break;
                case 3:
                    authorList();
                    break;
                case 4:
                    listLivingAuthors();
                    break;
                case 5:
                    listBookByLanguage();
                    break;
                case 6:
                    listTop10SearchedBooks();
                    break;
                case 7:
                    listTop10DownloadedBooks();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }


    }

    private void listTop10SearchedBooks() {
        System.out.println("----------------------------------------------------");
        System.out.println("Top 10 libros mas buscados");
        System.out.println("----------------------------------------------------");
        bookRepository.findByOrderByDownloadCountDesc().stream().limit(10)
                .forEach(book -> {
                    System.out.println("----------------------------------------------------");
                    System.out.println("Title: " + book.getTitle());
                    System.out.println("Autor: " + book.getAuthor().getName());
                    System.out.println("Idiomas: " + book.getLanguage());
                    System.out.println("Numero de descargas: " + book.getDownloadCount());
                    System.out.println("----------------------------------------------------");
                });
    }

    private void listTop10DownloadedBooks() {
        System.out.println("----------------------------------------------------");
        System.out.println("Top 10 libros mas descargados");
        System.out.println("----------------------------------------------------");
        var json = APIConsumer.getData(URL_BASE);
        var result = converter.convertData(json, Result.class);
        if (result.results().isEmpty()) {
            System.out.println("Titulo no encontrado");
        } else {
            result.results().stream()
                    .sorted(Comparator.comparing(Book::downloadCount).reversed())
                    .limit(10)
                    .limit(10)
                    .map(l -> l.title().toUpperCase())
                    .forEach(System.out::println);
            System.out.println("----------------------------------------------------");
        }
    }

    private void listBookByLanguage() {

        System.out.println("----------------------------------------------------");
        System.out.println("Escriba el idioma a buscar");
        var language = scanner.nextLine();

        System.out.println("----------------------------------------------------");
        System.out.println("Libros encontrados para el idioma " + language);

        List<BookEntity> result = bookRepository.findAll().stream().filter(book ->
                book.getLanguage().contains(language)).collect(Collectors.toList());

        result.forEach(book -> {
            System.out.println("----------------------------------------------------");
            System.out.println("Title: " + book.getTitle());
            //System.out.println("Autor: " + book.getAuthor().getName());
            System.out.println("Idiomas: " + book.getLanguage());
            System.out.println("Numero de descargas: " + book.getDownloadCount());
            System.out.println("----------------------------------------------------");
        });

        System.out.println("----------------------------------------------------");

    }


    private void searchedBookList() {

        System.out.println("----------------------------------------------------");
        System.out.println("Lista de libros buscados");

        bookRepository.findAll().stream().sorted(Comparator.comparing(BookEntity::getTitle)).forEach(book -> {
            System.out.println("----------------------------------------------------");
            System.out.println("Title: " + book.getTitle());
            System.out.println("Autor: " + book.getAuthor().getName());
            System.out.println("Idiomas: " + book.getLanguage());
            System.out.println("Numero de descargas: " + book.getDownloadCount());
            System.out.println("----------------------------------------------------");
        });


    }

    private void searchBookByTitle() {
        System.out.println("Escribe el nombre o palabra clave del libro que deseas buscar");
        var title = scanner.nextLine();
        var json = APIConsumer.getData(URL_BASE + "?search=" + title);
        var result = converter.convertData(json, Result.class);
        if (result.results().isEmpty()) {
            System.out.println("Titulo no encontrado");
        } else {
            Book book = result.results().get(0);
            if (bookRepository.findByTitle(book.title()) == null) {
                bookRepository.save(new BookEntity(book.title(), book.authors().get(0), book.languages(), book.downloadCount()));
            }
            System.out.println("----------------------------------------------------");
            System.out.println("Title: " + book.title());
            System.out.println("Autor: " + book.authors().get(0).name());
            System.out.println("Idioma: " + book.languages().get(0));
            System.out.println("Numero de descargas: " + book.downloadCount());
            System.out.println("----------------------------------------------------");
        }
    }

    private void authorList() {

        System.out.println("----------------------------------------------------");
        System.out.println("Lista  de Autores");

        authorRepository.findAll().stream().sorted(Comparator.comparing(AuthorEntity::getName)).forEach(author -> {
            System.out.println("----------------------------------------------------");
            System.out.println("Nombre: " + author.getName());
            System.out.println("Año de nacimiento: " + author.getBirthYear());
            System.out.println("Año de muerte: " + author.getDeathYear());
            System.out.println("----------------------------------------------------");
        });
    }

    private void listLivingAuthors() {

        System.out.println("Por favor digite el año");
        var year = scanner.nextLine();

        while (true) {
            if (!year.matches("^[1-2][0-9]{3}$")) {
                System.out.println("Por favor digite un año valido");
                year = scanner.nextLine();
            } else {
                break;
            }
        }

        authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(Integer.parseInt(year), Integer.parseInt(year)).stream()
                .sorted(Comparator.comparing(AuthorEntity::getName))
                .forEach(author -> {
                    System.out.println("----------------------------------------------------");
                    System.out.println("Nombre: " + author.getName());
                    System.out.println("Año de nacimiento: " + author.getBirthYear());
                    System.out.println("Año de muerte: " + author.getDeathYear());
                    System.out.println("----------------------------------------------------");
                });

    }


}
