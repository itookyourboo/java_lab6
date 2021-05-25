package client.lang;

import java.util.ListResourceBundle;

public class Resource_cs_CZ extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"login", "přihlášení"},
                {"password", "heslo"},
                {"enter", "Přihlášení"},
                {"register", "Registrace"},
                {"loginWindow", "Přihlášení"},
                {"locale", "národní prostředí"},
                {"back", "zpět"},

                {"stringBounds", "Délka řetězce musí být v rozsahu"},
                {"numberBounds", "Číslo musí být v rozsahu"},
                {"mustBeNotEmptyException", "Povinné pole"},
                {"numberFormatException", "očekává se číselná hodnota"},

                {"personWindow", "Okno pro správu"},
                {"name", "název"},
                {"weight", "hmotnost"},
                {"passport", "pas"},
                {"x", "X"},
                {"y", "Y"},
                {"location", "umístění"},
                {"studyGroupWindow", "Okno studijní skupiny"},
                {"id", "ID"},
                {"adminName", "jméno správce"},
                {"adminWeight", "Váha správce"},
                {"adminPassport", "cestovní pas správce"},
                {"studentsCount", "Počet studentů"},
                {"adminX", "správce X"},
                {"adminY", "správce Y"},
                {"expelledStudents", "Vyloučení studenti"},
                {"shouldBeExpelled", "Mělo by být odečteno"},
                {"adminLocation", "Umístění správce"},
                {"formOfEducation", "Forma vzdělávání"},
                {"cancel", "zrušit"},
                {"ok", "dobře"},

                {"tableWindow", "Hlavní nabídka"},
                {"addCommand", "Přidat skupinu"},
                {"logOut", "Odhlášení"},
                {"addIfMinCommand", "Přidat min. skupinu"},
                {"clearCommand", "Odebrat všechny skupiny"},
                {"countByGroupAdminCommand", "Počet skupin správce"},
                {"column", "sloupec"},
                {"executeScriptCommand", "provést skript"},
                {"criterion", "kritérium"},
                {"helpCommand", "Pomoc"},
                {"value", "hodnota"},
                {"infoCommand", "Informace"},
                {"removeByIdCommand", "Odebrat podle ID"},
                {"removeGreaterCommand", "Odebrat výše"},
                {"removeLowerCommand", "Odebrat níže"},
                {"updateCommand", "Aktualizovat skupinu"},
                {"visualizeCommand", "Vizualizace"},
                {"foundByAdmin", "Skupiny nalezeny"},
                {"sure", "Jste si jistý?"},
                {"helpMessage", "Zpráva nápovědy zde ..."},
                {"collectionType", "Typ kolekce"},
                {"collectionSize", "Velikost sbírky"},
                {"lastSave", "poslední uložení"},
                {"lastInit", "Poslední inicializace"},
                {"noSave", "Nebylo uloženo"},
                {"noInit", "Nebyla provedena žádná inicializace"},

                {"added", "úspěšně přidáno"},
                {"notAdded", "Nepodařilo se přidat"},
                {"removed", "odstraněn"},
                {"notRemoved", "Nepodařilo se smazat"},
                {"noSuchId", "Žádné takové ID neexistuje"},
                {"noAccess", "Nedostatečná práva"}
        };
    }
}
