package com.moaa.domain.books;

import com.moaa.domain.books.databases.BookDatabase;
import com.moaa.domain.books.properties.Author;
import com.moaa.domain.books.properties.Isbn;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Named
public class BookRepository {

    @Inject
    private BookDatabase bookDatabase;

    public List<Book> getBooks() {
        return bookDatabase.getBooks();
    }

    public Book getBook(String isbnString) {
        Book book = bookDatabase.getBooks().stream()
                .filter(b->b.getIsbn().equals(Isbn.convertStringToIsbn(isbnString)))
                .findFirst()
                .get();

        if (book == null) {
            throw new NoSuchElementException();
        }
        return book;
    }

    public Book createBook(Book book) {
        return bookDatabase.createBook(book);
    }

    public Book searchBookByIsbnPart(String isbnPart) {
        for (Book book : getBooks()) {
            if (book.getIsbn().toString().contains(isbnPart)) {
                return book;
            }
        }
        throw new IllegalArgumentException("Book not found.");
    }

    public Book searchBookByTitlePart(String titlePart) {
        for (Book book : getBooks()) {
            if (book.getTitle().contains(titlePart)) {
                return book;
            }
        }
        throw new IllegalArgumentException("Book not found.");
    }

    public Book searchBookByAuthorNamePart(String authorNamePart) {
        for (Book book : getBooks()) {
            if (book.getAuthor().getFirstName().concat(book.getAuthor().getLastName()).contains(authorNamePart)
                    || book.getAuthor().getLastName().concat(book.getAuthor().getFirstName()).contains(authorNamePart)) {
                return book;
            }
        }
        throw new IllegalArgumentException("Book not found.");
    }

    public void deleteBook(String isbnString) {
        try {
            bookDatabase.delete(searchBookByIsbnPart(isbnString));
        } catch (IllegalArgumentException e) {
            System.out.println("No book found with the given ISBN.");
        }
    }

    public Book updateBook(String isbnString, Book book) {
        deleteBook(isbnString);
        return createBook(book);
    }

    /*public String showDetailsOfBook(Isbn isbn) {
        String detailsOfBook = bookDatabase.getBooks().stream()
                .filter(b->b.getIsbn().equals(isbn))
                .findFirst()
                .get()
                .toString();
        if (detailsOfBook == null) {
            throw new NoSuchElementException();
        }
        return detailsOfBook;
    }*/
}
