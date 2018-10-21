# ft_lct3
Проект по созданию базы данных физических лиц и заполнению этими данными excel файл

Чтобы запустить проект из консоли надо:

добавить maven и JAVA_HOME в переменную окружения PATH (то есть .../maven/bin и .../Java/jdk-10.0.2 (моя версия JDK))
посмотреть, работает ли mvn -v (у меня версия 3.5.4)
собрать проект mvn compile
запустить проект: mvn exec:java -Dexec.mainClass="DataWorker"

Файл с данными для подключения к базе хранится в src/main/recources и называется dataForConnection.txt
Устроен так:
ААА - URL БД
ВВВ - логин
ССС - пароль
