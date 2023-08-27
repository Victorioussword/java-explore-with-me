# java-explore-with-me

## Яндекс Практикум: проект "Explore With Me"

Explore With Me - соц. сеть для обмена информацией о событиях
Стек: REST-сервис с использованием Spring Boot, Maven, Lombok, Doker, PostgreSQL



Многомодульный проект состоящий из:
- сервиса сбора статистики по переходам по ссылке;
- основного сервиса с бизнес логикой;
- клиента для запроса статистики.
  
  Общение между сервисами происходит через REST API.
  Каждый сервис запускается в отдельном Docker-контейнере.
  Хранение информации осуществляется в базах данных PostreSQL запущенных в Docker-контейнерах.

### Ссылки для ревьюера:
[сервис статистики](https://github.com/Victorioussword/java-explore-with-me/pull/3)
[Основной сервис](https://github.com/Victorioussword/java-explore-with-me/pull/4)
[фича](https://github.com/Victorioussword/java-explore-with-me/pull/5)

