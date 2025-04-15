import React, { useState } from 'react';
import BookList from './components/BookList';
import BookForm from './components/BookForm';
import { Book } from './models/Book';

const App: React.FC = () => {
    const [editingBook, setEditingBook] = useState<Book | undefined>(undefined);
    const [refreshKey, setRefreshKey] = useState(0);
    const [formKey, setFormKey] = useState(0);

    const handleEdit = (book: Book) => {
        setEditingBook(book);
        setFormKey(prev => prev + 1);
    };

    const handleSuccess = () => {
        setEditingBook(undefined);
        setRefreshKey(prev => prev + 1);
        setFormKey(prev => prev + 1);
    };

    return (
        <div style={{ padding: '20px' }}>
            <div style={{ display: 'flex', gap: '20px' }}>
                <div style={{ flex: 1 }}>
                    <BookList
                        key={refreshKey}
                        onEdit={handleEdit}
                    />
                </div>
                <div style={{ flex: 1 }}>
                    <h2>{editingBook ? 'Edit Book' : 'Add New Book'}</h2>
                    <BookForm
                        key={formKey}
                        book={editingBook}
                        onSuccess={handleSuccess}
                    />
                </div>
            </div>
        </div>
    );
};

export default App;
