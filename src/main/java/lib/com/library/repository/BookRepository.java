package lib.com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lib.com.library.model.BookModel;

public interface BookRepository extends JpaRepository<BookModel, Long> {
}
