import React from 'react';
import { Book } from '../models/Book';

interface BookItemProps {
    book: Book;
    onDelete: (id: number) => void;
    onEdit: (book: Book) => void;
}

const BookItem: React.FC<BookItemProps> = ({ book, onDelete, onEdit }) => {
    return (
        <div style={{ border: '1px solid #ccc', padding: '10px', margin: '10px 0' }}>
            <h3>{book.title}</h3>
            <p>Author: {book.author}</p>
            <p>Year: {book.publicationYear}</p>
            <p>ISBN: {book.isbn}</p>
            <button onClick={() => onEdit(book)}>Edit</button>
            <button onClick={() => onDelete(book.id!)}>Delete</button>
        </div>
    );
};

export default BookItem;
