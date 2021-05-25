package client.lang;

import java.util.ListResourceBundle;

public class Resource_bg_BG extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"login", "Вход"},
                {"password", "Парола"},
                {"enter", "Вход"},
                {"register", "Регистрация"},
                {"loginWindow", "Вход"},
                {"locale", "Език"},
                {"back", "Назад"},

                {"stringBounds", "Дължината на низа трябва да бъде в диапазона"},
                {"numberBounds", "Броят трябва да бъде в диапазона"},
                {"mustBeNotEmptyException", "Полето е задължително за попълване"},
                {"numberFormatException", "Очаква се цифрова стойност"},

                {"personWindow", "Прозорец Администратор"},
                {"name", "Име"},
                {"weight", "Тегло"},
                {"passport", "Паспорт"},
                {"x", "X"},
                {"y", "Y"},
                {"location", "Местоположение"},
                {"studyGroupWindow", "Прозорец на учебната група"},
                {"id", "ID"},
                {"adminName", "Име на администратор"},
                {"adminWeight", "Тегло администратор"},
                {"adminPassport", "Администратор Паспорт"},
                {"studentsCount", "Брой студенти"},
                {"adminX", "X Администратор"},
                {"adminY", "Y Администратор"},
                {"expelledStudents", "Изключен студенти"},
                {"shouldBeExpelled", "Трябва да се изключи"},
                {"adminLocation", "Местоположение администратор"},
                {"formOfEducation", "Форма на образование"},
                {"cancel", "Отказ"},
                {"ok", "ОК"},

                {"tableWindow", "Главното меню"},
                {"addCommand", "Добавяне на група"},
                {"logOut", "Изход"},
                {"addIfMinCommand", "Добавяне на мин. група"},
                {"clearCommand", "Изтриване на всички групи"},
                {"countByGroupAdminCommand", "Брой групи администратор"},
                {"column", "Колона"},
                {"executeScriptCommand", "Изпълнение на скрипт"},
                {"criterion", "Критерий"},
                {"helpCommand", "Помощ"},
                {"value", "Стойност"},
                {"infoCommand", "Информация"},
                {"removeByIdCommand", "Изтриване по ID"},
                {"removeGreaterCommand", "Изтриване по-горе"},
                {"removeLowerCommand", "Изтриване по-долу"},
                {"updateCommand", "Обновяване на групата"},
                {"visualizeCommand", "Визуализация"},
                {"foundByAdmin", "Намерени групи"},
                {"sure", "Сигурен ли си?"},
                {"helpMessage", "Има съобщение за помощ..."},
                {"collectionType", "Тип колекция"},
                {"collectionSize", "Размер на колекцията"},
                {"lastSave", "Последно запазване"},
                {"lastInit", "Последна инициализация"},
                {"noSave", "Няма запис"},
                {"noInit", "Няма инициализация"},

                {"added", "Добавено успешно"},
                {"notAdded", "Неуспешно добавяне"},
                {"removed", "Премахнато"},
                {"notRemoved", "Неуспешно изтриване"},
                {"noSuchId", "Не съществува такъв идентификатор"},
                {"noAccess", "Недостатъчни права"}
        };
    }
}
