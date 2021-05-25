package client.lang;

import java.util.ListResourceBundle;

public class Resource_ru_RU extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"login", "Логин"},
                {"password", "Пароль"},
                {"enter", "Войти"},
                {"register", "Регистрация"},
                {"loginWindow", "Вход в систему"},
                {"locale", "Язык"},
                {"back", "Назад"},

                {"stringBounds", "Длина строки должна быть в диапазоне"},
                {"numberBounds", "Число должно быть в диапазоне"},
                {"mustBeNotEmptyException", "Поле обязательно для заполнения"},
                {"numberFormatException", "Ожидается числовое значение"},

                {"personWindow", "Окно админа"},
                {"name", "Имя"},
                {"weight", "Вес"},
                {"passport", "Паспорт"},
                {"x", "X"},
                {"y", "Y"},
                {"location", "Локация"},
                {"studyGroupWindow", "Окно учебной группы"},
                {"id", "ID"},
                {"adminName", "Имя админа"},
                {"adminWeight", "Вес админа"},
                {"adminPassport", "Паспорт админа"},
                {"studentsCount", "Количество студентов"},
                {"adminX", "X админа"},
                {"adminY", "Y админа"},
                {"expelledStudents", "Отчислено студентов"},
                {"shouldBeExpelled", "Надо отчислить"},
                {"adminLocation", "Локация админа"},
                {"formOfEducation", "Форма образования"},
                {"cancel", "Отмена"},
                {"ok", "ОК"},

                {"tableWindow", "Главное меню"},
                {"addCommand", "Добавить группу"},
                {"logOut", "Выход из системы"},
                {"addIfMinCommand", "Добавить мин. группу"},
                {"clearCommand", "Удалить все группы"},
                {"countByGroupAdminCommand", "Количество групп админа"},
                {"column", "Столбец"},
                {"executeScriptCommand", "Выполнить скрипт"},
                {"criterion", "Критерий"},
                {"helpCommand", "Помощь"},
                {"value", "Значение"},
                {"infoCommand", "Информация"},
                {"removeByIdCommand", "Удалить по ID"},
                {"removeGreaterCommand", "Удалить выше"},
                {"removeLowerCommand", "Удалить ниже"},
                {"updateCommand", "Обновить группу"},
                {"visualizeCommand", "Визуализация"},
                {"foundByAdmin", "Найдено групп"},
                {"sure", "Вы уверены?"},
                {"helpMessage", "Тут сообщение о помощи..."},
                {"collectionType", "Тип коллекции"},
                {"collectionSize", "Размер коллекции"},
                {"lastSave", "Последнее сохранение"},
                {"lastInit", "Последняя инициализация"},
                {"noSave", "Сохранения не было"},
                {"noInit", "Инициализации не было"},

                {"added", "Успешно добавлено"},
                {"notAdded", "Не удалось добавить"},
                {"removed", "Удалено"},
                {"notRemoved", "Не удалось удалить"},
                {"noSuchId", "Не существует такого ID"},
                {"noAccess", "Недостаточно прав"}
        };
    }
}
