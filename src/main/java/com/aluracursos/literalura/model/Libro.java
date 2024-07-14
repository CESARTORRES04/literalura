package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String idiomas;
    private Long numeroDescargas;
    @OneToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Libro() {
    }

    public Libro(String titulo, String idiomas, Long numeroDescargas) {
        this.titulo = titulo;
        this.idiomas = idiomas;
        this.numeroDescargas = numeroDescargas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Long getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Long numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    @Override
    public String toString() {
        return "-----LIBRO-----" + "\n"+
                "Titulo: " + titulo + "\n"+
                "Autor: " + autor.getNombre() + "\n"+
                "Idioma: " + idiomas + "\n"+
                "Numero de descargas: " + numeroDescargas + "\n"+
                "------------";
    }
}
