
                            #
                             Сравнение: new внутри vs DI через конструктор - DI через конструктор позвоялет подставить мок объект 
                            
```java 
public class LeadService {
    private final LeadRepository repository;

    public LeadService(LeadRepository repository) {
        this.repository = repository;
    }
}

                            #
                            # BAD: new InMemoryLeadRepository() внутри класса жеско зашита реализация внутри класс, класс сам отвечает за создание объекта

```java
public class LeadService {
    // Тесная связанность!

                                       private final LeadRepository repository =
                                    new InMemoryLeadRepository();