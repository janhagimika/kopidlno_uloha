Stažení xml souboru, parsování souboru a následné uložení dat do databáze.

# kopidlno_uloha
pro úlohu využita databáze postgres - připojení v application.properties
tabulky vytvořeny pomocí 
CREATE TABLE obec (
    kod BIGINT PRIMARY KEY,
    nazev VARCHAR(255)
);

CREATE TABLE cast_obce (
    kod BIGINT PRIMARY KEY,
    nazev VARCHAR(255),
    obec_kod BIGINT,
    FOREIGN KEY (obec_kod) REFERENCES obec(kod)
);

pro parsování využit StAX

pro přehlednost ponechány některé testovací logy(system.out.println)
