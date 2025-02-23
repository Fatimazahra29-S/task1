import java.io.*;
import java.util.*;

class Book implements Serializable {
    private int id;
    private String title;
    private String author;
    private int year;

    public Book(int id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }

    @Override
    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Author: " + author + ", Year: " + year;
    }
}

class Library {
    private List<Book> books;
    private final String FILE_NAME = "books.dat";

    public Library() {
        books = new ArrayList<>();
        loadBooksFromFile();
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooksToFile();
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            books.forEach(System.out::println);
        }
    }

    public void searchBookByTitle(String title) {
        books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .forEach(System.out::println);
    }

    public void deleteBookById(int id) {
        books.removeIf(book -> book.getId() == id);
        saveBooksToFile();
    }

    private void saveBooksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private void loadBooksFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            books = (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            books = new ArrayList<>();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.println("\nLibrary Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Show All Books");
            System.out.println("3. Search Book by Title");
            System.out.println("4. Delete Book");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter book ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter author: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter year: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();

                        library.addBook(new Book(id, title, author, year));
                        System.out.println("Book added successfully!");
                        break;
                    case 2:
                        library.displayBooks();
                        break;
                    case 3:
                        System.out.print("Enter title to search: ");
                        String searchTitle = scanner.nextLine();
                        library.searchBookByTitle(searchTitle);
                        break;
                    case 4:
                        System.out.print("Enter book ID to delete: ");
                        int deleteId = scanner.nextInt();
                        scanner.nextLine();
                        library.deleteBookById(deleteId);
                        System.out.println("Book deleted successfully!");
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a valid number.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }
}
