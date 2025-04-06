import React, { useState } from 'react';
import BookList from './components/BookList';
import BookForm from './components/BookForm';
import { Book } from './models/Book';

const App: React.FC = () => {
    const [editingBook, setEditingBook] = useState<Book | undefined>(undefined);
    const [refreshKey, setRefreshKey] = useState(0);

    const handleEdit = (book: Book) => {
        setEditingBook(book);
    };

    const handleSuccess = () => {
        setEditingBook(undefined);
        setRefreshKey(prev => prev + 1);
    };

    return (
        <div style={{ padding: '20px' }}>
            <h1>Library Management</h1>
            <div style={{ display: 'flex', gap: '20px' }}>
                <div style={{ flex: 1 }}>
                    <h2>Books</h2>
                    <BookList
                        key={refreshKey}
                        onEdit={handleEdit}
                    />
                </div>
                <div style={{ flex: 1 }}>
                    <h2>{editingBook ? 'Edit Book' : 'Add New Book'}</h2>
                    <BookForm
                        book={editingBook}
                        onSuccess={handleSuccess}
                    />
                </div>
            </div>
        </div>
    );
};

export default App;
