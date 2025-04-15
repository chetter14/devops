import React, { useState, useEffect } from 'react'; // Добавлен импорт useEffect
import { Book } from '../models/Book';
import { createBook, updateBook } from '../services/BookService';

interface BookFormProps {
    book?: Book;
    onSuccess: () => void;
}

const BookForm: React.FC<BookFormProps> = ({ book, onSuccess }) => {
    const [formData, setFormData] = useState<Book>({
        title: '',
        author: '',
        publicationYear: new Date().getFullYear(),
        isbn: ''
    });
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (book) {
            setFormData(book);
        } else {
            setFormData({
                title: '',
                author: '',
                publicationYear: new Date().getFullYear(),
                isbn: ''
            });
        }
    }, [book]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'publicationYear' ? parseInt(value) || 0 : value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        if (!formData.title.trim() || !formData.author.trim() ||
            !formData.publicationYear || !formData.isbn.trim()) {
            setError('All fields are required');
            return;
        }

        try {
            if (formData.id) {
                await updateBook(formData.id, formData);
            } else {
                await createBook(formData);
            }
            onSuccess();
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to save book');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            {error && <div className="error">{error}</div>}
            <div>
                <label htmlFor="title">Title:</label>
                <input
                    id="title"
                    type="text"
                    name="title"
                    value={formData.title}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="author">Author:</label>
                <input
                    id="author"
                    type="text"
                    name="author"
                    value={formData.author}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="publicationYear">Publication Year:</label>
                <input
                    id="publicationYear"
                    type="number"
                    name="publicationYear"
                    value={formData.publicationYear}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label htmlFor="isbn">ISBN:</label>
                <input
                    id="isbn"
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
