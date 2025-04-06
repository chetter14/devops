import React, { useState } from 'react';
import { Book } from '../models/Book';
import { createBook, updateBook } from '../services/BookService';

interface BookFormProps {
    book?: Book;
    onSuccess: () => void;
}

const BookForm: React.FC<BookFormProps> = ({ book, onSuccess }) => {
    const [formData, setFormData] = useState<Book>(book || {
        title: '',
        author: '',
        publicationYear: new Date().getFullYear(),
        isbn: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'publicationYear' ? parseInt(value) : value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            if (formData.id) {
                await updateBook(formData.id, formData);
            } else {
                await createBook(formData);
            }
            onSuccess();
        } catch (error) {
            console.error('Error saving book:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Title:</label>
                <input
                    type="text"
                    name="title"
                    value={formData.title}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>Author:</label>
                <input
                    type="text"
                    name="author"
                    value={formData.author}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>Publication Year:</label>
                <input
                    type="number"
                    name="publicationYear"
                    value={formData.publicationYear}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>ISBN:</label>
                <input
                    type="text"
                    name="isbn"
                    value={formData.isbn}
                    onChange={handleChange}
                    required
                />
            </div>
            <button type="submit">Save</button>
        </form>
    );
};

export default BookForm;
