import { Book } from "../models/Book";

const API_URL = 'http://localhost:8080/api/books';

export const getBooks = async (): Promise<Book[]> => {
    const response = await fetch(API_URL);
    return await response.json();
};

export const createBook = async (book: Book): Promise<Book> => {
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(book),
    });
    return await response.json();
};

export const updateBook = async (id: number, book: Book): Promise<Book> => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(book),
    });
    return await response.json();
};

export const deleteBook = async (id: number): Promise<void> => {
    await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
    });
};
