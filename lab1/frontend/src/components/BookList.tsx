import React, { useEffect, useState } from 'react';
import { Book } from '../models/Book';
import { getBooks, deleteBook } from '../services/BookService';
import BookItem from './BookItem';

interface BookListProps {
    onEdit: (book: Book) => void;
}

const BookList: React.FC<BookListProps> = ({ onEdit }) => {
    const [books, setBooks] = useState<Book[]>([]);

    useEffect(() => {
        const fetchBooks = async () => {
            const books = await getBooks();
            setBooks(books);
        };
        fetchBooks();
    }, []);

    const handleDelete = async (id: number) => {
        await deleteBook(id);
        setBooks(books.filter(book => book.id !== id));
    };

    return (
        <div>
            <h2>Book List</h2>
            {books.map(book => (
                <BookItem
                    key={book.id}
                    book={book}
                    onDelete={handleDelete}
                    onEdit={onEdit}
                />
            ))}
        </div>
    );
};

export default BookList;
