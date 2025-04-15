import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import BookForm from './BookForm';
import { Book } from '../models/Book';
import * as BookService from '../services/BookService';

jest.mock('../services/BookService');

describe('BookForm', () => {
  const mockOnSuccess = jest.fn();
  const mockBook: Book = {
    id: 1,
    title: 'Existing Book',
    author: 'Existing Author',
    publicationYear: 2020,
    isbn: '1234567890'
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders empty form for new book', () => {
    render(<BookForm onSuccess={mockOnSuccess} />);

    expect(screen.getByLabelText('Title:')).toBeInTheDocument();
    expect(screen.getByLabelText('Author:')).toBeInTheDocument();
    expect(screen.getByLabelText('Publication Year:')).toBeInTheDocument();
    expect(screen.getByLabelText('ISBN:')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Save' })).toBeInTheDocument();
  });

  it('renders filled form for existing book', () => {
    render(<BookForm book={mockBook} onSuccess={mockOnSuccess} />);

    expect(screen.getByLabelText('Title:')).toHaveValue('Existing Book');
    expect(screen.getByLabelText('Author:')).toHaveValue('Existing Author');
    expect(screen.getByLabelText('Publication Year:')).toHaveValue(2020);
    expect(screen.getByLabelText('ISBN:')).toHaveValue('1234567890');
  });

  it('updates form fields when user types', () => {
    render(<BookForm onSuccess={mockOnSuccess} />);

    fireEvent.change(screen.getByLabelText('Title:'), { target: { value: 'New Title' } });
    fireEvent.change(screen.getByLabelText('Author:'), { target: { value: 'New Author' } });
    fireEvent.change(screen.getByLabelText('Publication Year:'), { target: { value: '2023' } });
    fireEvent.change(screen.getByLabelText('ISBN:'), { target: { value: '9876543210' } });

    expect(screen.getByLabelText('Title:')).toHaveValue('New Title');
    expect(screen.getByLabelText('Author:')).toHaveValue('New Author');
    expect(screen.getByLabelText('Publication Year:')).toHaveValue(2023);
    expect(screen.getByLabelText('ISBN:')).toHaveValue('9876543210');
  });

  it('submits new book form successfully', async () => {
    const newBook = {
      title: 'New Book',
      author: 'New Author',
      publicationYear: 2023,
      isbn: '9876543210'
    };
    (BookService.createBook as jest.Mock).mockResolvedValue({ id: 2, ...newBook });

    render(<BookForm onSuccess={mockOnSuccess} />);

    fireEvent.change(screen.getByLabelText('Title:'), { target: { value: newBook.title } });
    fireEvent.change(screen.getByLabelText('Author:'), { target: { value: newBook.author } });
    fireEvent.change(screen.getByLabelText('Publication Year:'), { target: { value: newBook.publicationYear.toString() } });
    fireEvent.change(screen.getByLabelText('ISBN:'), { target: { value: newBook.isbn } });

    fireEvent.click(screen.getByRole('button', { name: 'Save' }));

    await waitFor(() => {
      expect(BookService.createBook).toHaveBeenCalledWith({
        title: 'New Book',
        author: 'New Author',
        publicationYear: 2023,
        isbn: '9876543210'
      });
      expect(mockOnSuccess).toHaveBeenCalled();
    });
  });

  it('shows error when required fields are empty', async () => {
    render(<BookForm onSuccess={mockOnSuccess} />);

    fireEvent.click(screen.getByRole('button', { name: 'Save' }));

    await waitFor(() => {
      expect(screen.getByText('All fields are required')).toBeInTheDocument();
      expect(BookService.createBook).not.toHaveBeenCalled();
    });
  });
});
