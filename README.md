# literalura


El presente proyecto permite al usuario consultar informacion relacionada con libros y autores. El menu que ofrece es el siguiente:

- Buscar libro por titulo
- Lista de libros buscados
-  Lista de Autores
-  Lista de autores vivos en determiaño
-  Listar libros en un idioma
-  Listar el top 10 de los librosbuscados
-  Listar el top 10 de los libros mas descargados



 ## Desarrollo

 El proyecto se encuentra desarrollado con Java 17, SpringBoot 3.3 utiliza la libreria Jackson para transformar JSON en objetos e internamente consume un servicio utilizando el HttpClient, el cual retorna una lista de libros junto con sus autores. Adicional implementa JPA y almacena datos en una base de datos postgresSQL
