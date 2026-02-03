# BCORE-13: Рефакторинг LeadListServlet на JTE

## Цель
Замена 50 строк `println()` в `LeadListServlet` на JTE шаблоны с Tailwind CSS для улучшения поддерживаемости, безопасности и разделения ответственности.

## Выполненные задачи

### 1. Настройка JTE в проекте
- Добавлен JTE Gradle plugin версии 3.1.15
- Добавлены зависимости: `gg.jte:jte` и `gg.jte:jte-runtime`
- Настроен sourceDirectory для шаблонов: `src/main/jte`

### 2. Создание базового layout
- Создан `src/main/jte/layout/main.jte` с параметром `@param gg.jte.Content content`
- Подключен Tailwind CSS через CDN для быстрого прототипирования
- Реализована общая структура страниц: header, main content area, footer

### 3. Создание шаблона для списка лидов
- Создан `src/main/jte/leads/list.jte` с type-safe параметром `@param java.util.List<Lead> leads`
- Реализована HTML таблица с Tailwind utility-классами
- Использован цикл `@for(var lead : leads)` для генерации строк таблицы
- Данные выводятся через `${lead.contact().email()}`, `${lead.company()}`, `${lead.status()}` с автоматическим XSS экранированием

### 4. Рефакторинг LeadListServlet
- Добавлено поле `private TemplateEngine templateEngine`
- Инициализация TemplateEngine в методе `init()` с использованием `DirectoryCodeResolver`
- В методе `doGet()` заменены ~50 строк `writer.println()` на вызов `templateEngine.render("leads/list.jte", model, output)`
- Создание модели данных через `Map<String, Object>` для передачи в шаблон

### 5. Обновление тестов
- Тесты `LeadListServletTest` переписаны для работы с новой архитектурой
- Исправлены проверки под новый HTML, генерируемый JTE + Tailwind
- Устранены проблемы с `UnnecessaryStubbingException`

## Результаты рефакторинга

| Критерий | BCORE-12 (старый println) | BCORE-13 (JTE + Tailwind) | Преимущество |
|----------|---------------------------|----------------------------|--------------|
| **Строк Java кода** | ~50 строк в `doGet()` | ~7 строк в `doGet()` | Уменьшение кода в 7 раз |
| **Разделение ответственности** | Java + HTML смешаны | Java (логика) ↔ JTE (представление) | Чистая архитектура |
| **Безопасность (XSS)** | Ручное экранирование | Автоматическое через `${}` | Защита по умолчанию |
| **Type-safety** | Runtime ошибки | Compile-time проверки | Раннее обнаружение ошибок |
| **Переиспользование** | Копипаст кода | Layout `main.jte` | Следование принципу DRY |
| **Стилизация** | Нет/простые CSS | Tailwind utility-классы | Современный, отзывчивый UI |

## Ключевые преимущества JTE

### Type-safety параметров
```jte
@param java.util.List<ru.mentee.power.crm.domain.Lead> leads