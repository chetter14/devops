import React from 'react';
import { render, screen, waitFor, act } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import App from './App';
import * as BookService from './services/BookService';
import { Book } from './models/Book';

jest.mock('./services/BookService');

const mockBooks: Book[] = [
  {
    id: 1,
    title: 'Test Book 1',
    author: 'Author 1',
    publicationYear: 2021,
    isbn: '1111111111'
  },
  {
    id: 2,
    title: 'Test Book 2',
    author: 'Author 2',
    publicationYear: 2022,
    isbn: '2222222222'
  }
];

describe('App', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    (BookService.getBooks as jest.Mock).mockResolvedValue(mockBooks);
    (BookService.createBook as jest.Mock).mockImplementation((book: Book) =>
      Promise.resolve({ ...book, id: mockBooks.length + 1 })
    );
    (BookService.updateBook as jest.Mock).mockImplementation((id: number, book: Book) =>
      Promise.resolve({...mockBooks.find(b => b.id === id), ...book})
    );
    (BookService.deleteBook as jest.Mock).mockResolvedValue({});
  });

  it('renders the app with book list and form', async () => {
    await act(async () => {
      render(<App />);
    });

    expect(await screen.findByText('Test Book 1')).toBeInTheDocument();
    expect(await screen.findByText('Test Book 2')).toBeInTheDocument();
    expect(screen.getByLabelText('Title:')).toBeInTheDocument();
  });

    it('adds a new book', async () => {
      await act(async () => {
        render(<App />);
      });

      await screen.findByText('Test Book 1');

      await act(async () => {
        await userEvent.type(screen.getByLabelText('Title:'), 'New Book');
        await userEvent.type(screen.getByLabelText('Author:'), 'New Author');

        const yearInput = screen.getByLabelText('Publication Year:');
        await userEvent.clear(yearInput);
        await userEvent.type(yearInput, '2023');

        await userEvent.type(screen.getByLabelText('ISBN:'), '3333333333');
      });

      await act(async () => {
        await userEvent.click(screen.getByRole('button', { name: 'Save' }));
      });

      await waitFor(() => {
        expect(BookService.createBook).toHaveBeenCalledWith({
          title: 'New Book',
          author: 'New Author',
          publicationYear: 2023,
          isbn: '3333333333'
        });
      });
    });

  it('edits an existing book', async () => {
    await act(async () => {
      render(<App />);
    });

    await screen.findByText('Test Book 1');

    const editButtons = await screen.findAllByRole('button', { name: /edit/i });
    await act(async () => {
      await userEvent.click(editButtons[0]);
    });

    await waitFor(() => {
      expect(screen.getByDisplayValue('Test Book 1')).toBeInTheDocument();
    });

    await act(async () => {
      await userEvent.clear(screen.getByLabelText('Title:'));
      await userEvent.type(screen.getByLabelText('Title:'), 'Updated Title');
      await userEvent.click(screen.getByRole('button', { name: 'Save' }));
    });

    expect(BookService.updateBook).toHaveBeenCalledWith(1, {
      id: 1,
      title: 'Updated Title',
      author: 'Author 1',
      publicationYear: 2021,
      isbn: '1111111111'
    });
  });

  it('deletes a book', async () => {
    await act(async () => {
      render(<App />);
    });

    await screen.findByText('Test Book 1');

    const deleteButtons = await screen.findAllByRole('button', { name: /delete/i });

    await act(async () => {
      await userEvent.click(deleteButtons[0]);
    });

    await waitFor(() => {
      expect(BookService.deleteBook).toHaveBeenCalledWith(1);
    });
  });

  it('switches between add and edit modes', async () => {
    await act(async () => {
      render(<App />);
    });

    await screen.findByText('Test Book 1');

    expect(screen.getByText('Add New Book')).toBeInTheDocument();

    const editButtons = await screen.findAllByRole('button', { name: /edit/i });

    await act(async () => {
      await userEvent.click(editButtons[0]);
    });

    await waitFor(() => {
      expect(screen.getByText('Edit Book')).toBeInTheDocument();
    });

    await act(async () => {
      await userEvent.click(screen.getByRole('button', { name: 'Save' }));
    });

    await waitFor(() => {
      expect(screen.getByText('Add New Book')).toBeInTheDocument();
    });
  });
});
