CREATE TABLE IF NOT EXISTS FamilleAliment(
    FamilleAlimentID INTEGER PRIMARY KEY ,
    Nom CHAR(20) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS Unite(
    UniteID INTEGER PRIMARY KEY ,
    Nom CHAR(10) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS Categorie(
    CategorieID INTEGER PRIMARY KEY ,
    Nom CHAR(20) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS TypePlat(
    TypePlatID INTEGER PRIMARY KEY ,
    Nom CHAR(20) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS Recette(
    RecetteID INTEGER PRIMARY KEY ,
    Nom CHAR(40) NOT NULL,
    Duree INTEGER NOT NULL,
    NbPersonnes INTEGER NOT NULL,
    TypePlatID INTEGER NOT NULL
        CONSTRAINT fkTypePlat REFERENCES TypePlat(TypePlatID),
    CategorieID INTEGER NOT NULL
        CONSTRAINT fkCategorie REFERENCES Categorie(CategorieID),
    Preparation TEXT NOT NULL
    )

CREATE TABLE IF NOT EXISTS Ingredient(
    IngredientID INTEGER PRIMARY KEY ,
    Nom CHAR(25) NOT NULL UNIQUE,
    FamilleAlimentID INTEGER
        CONSTRAINT fkFamilleAliment REFERENCES FamilleAliment(FamilleAlimentID),
    UniteID INTEGER
        CONSTRAINT fkUnite REFERENCES Unite(UniteID)
    )

CREATE TABLE IF NOT EXISTS RecetteIngredient(
    RecetteID INTEGER NOT NULL
        CONSTRAINT fkRecette REFERENCES Recette(RecetteID),
    IngredientID INTEGER NOT NULL
        CONSTRAINT fkIngredient REFERENCES Ingredient(IngredientID),
    Quantite INTEGER NOT NULL,
    PRIMARY KEY (RecetteID, IngredientID)
    )

CREATE TABLE IF NOT EXISTS ListeCourse(
    CREATE TABLE IF NOT EXISTS ListeCourse (
    ListeCourseID INTEGER PRIMARY KEY AUTOINCREMENT,
    Nom CHAR(25) NOT NULL UNIQUE)
)

CREATE TABLE IF NOT EXISTS ListeCourseIngredient(
    ListeCourseID INTEGER
        CONSTRAINT fkListeCourse REFERENCES ListeCourse(ListeCourseID),
    IngredientID INTEGER
        CONSTRAINT fkIngredient REFERENCES Ingredient(IngredientID),
    Quantite INTEGER NOT NULL,
    PRIMARY KEY (ListeCourseID, IngredientID)
    )

CREATE TABLE IF NOT EXISTS MenuRecette(
    Date DATE NOT NULL,
    Heure TIME,
    RecetteID INTEGER
        CONSTRAINT fkRecette REFERENCES Recette(RecetteID),
    PRIMARY KEY (Date, Heure)
    )
