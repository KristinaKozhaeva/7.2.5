package exercise.article;

import exercise.worker.WorkerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkerImplTest {
    private List<Article> commonArticles;
    private Library mockLibrary;
    private WorkerImpl worker;

    @BeforeEach
    public void setUp() {
        commonArticles = Arrays.asList(new Article("Article 1", "Content 1", "Author 1", LocalDate.of(2022, 1, 1)),
                new Article("Article 2", "Content 2", "Author 2", null),
                new Article(null, "Content 3", "Author 3", LocalDate.of(2022, 1, 1)),
                new Article("Article 4", null, "Author 4", LocalDate.of(2024, 1, 1)),
                new Article("Article 5", "Content 5", null, LocalDate.of(2024, 1, 1))
        );
        mockLibrary = mock(Library.class);
        worker = new WorkerImpl(mockLibrary);
    }


    @DisplayName("Проверка группировки по году написания статьи")
    @Test
    void SortArticles() {
        worker.addNewArticles(commonArticles);
        verify(mockLibrary).store(eq(2022), anyList());
        verify(mockLibrary).store(eq(2024), anyList());
    }

    @DisplayName("Получаем статьи из каталога")
    @Test
    void testGetCatalog() {
        List<String> titles = Arrays.asList("Article 1", "Article 2");
        when(mockLibrary.getAllTitles()).thenReturn(titles);
        String catalog = worker.getCatalog();
        String expectedCatalog = "Список доступных статей:\n" +
                "    Article 1\n" +
                "    Article 2\n";
        assertEquals(expectedCatalog, catalog);
    }

    @DisplayName("Проверяем, что сохраняются только валидные статьи")
    @Test
    void prepareArticles() {
        List<Article> preparedArticles = worker.prepareArticles(commonArticles);
        assertEquals(2, preparedArticles.size());

    }
    @DisplayName("Проверяем, что проставляются даты, если они не указаны")
    @Test
    void SetDate() {
        List<Article> preparedArticles = worker.prepareArticles(commonArticles);
        for (Article article: preparedArticles) {
            if (article.getCreationDate() == null) {
                assertEquals(LocalDate.now(), article.getCreationDate());
            }
        }

    }
}





