CREATE TABLE IF NOT EXISTS FamilleAliment(
                                             FamilleAlimentID INTEGER PRIMARY KEY AUTOINCREMENT,
                                             Nom CHAR(20) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS Unite(
                                    UniteID INTEGER PRIMARY KEY AUTOINCREMENT,
                                    Nom CHAR(10) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS Categorie(
                                        CategorieID INTEGER PRIMARY KEY AUTOINCREMENT,
                                        Nom CHAR(20) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS TypePlat(
                                       TypePlatID INTEGER PRIMARY KEY AUTOINCREMENT,
                                       Nom CHAR(20) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS Recette(
                                      RecetteID INTEGER PRIMARY KEY AUTOINCREMENT,
                                      Nom CHAR(50) NOT NULL,
    Duree INTEGER NOT NULL,
    NbPersonnes INTEGER NOT NULL,
    TypePlatID INTEGER NOT NULL,
    FOREIGN KEY(TypePlatID) REFERENCES TypePlat(TypePlatID),
    CategorieID INTEGER NOT NULL,
    FOREIGN KEY(CategorieID) REFERENCES Categorie(CategorieID),
    Preparation TEXT NOT NULL
    )

CREATE TABLE IF NOT EXISTS RecetteIngredient(
                                                RecetteID INTEGER NOT NULL,
                                                FOREIGN KEY(RecetteID) REFERENCES Recette(RecetteID),
    IngredientID INTEGER NOT NULL,
    FOREIGN KEY(IngredientID) REFERENCES Ingredient(IngredientID),
    Quantite INTEGER NOT NULL,
    PRIMARY KEY (RecetteID, IngredientID)
    )

CREATE TABLE IF NOT EXISTS Ingredient(
                                         IngredientID INTEGER PRIMARY KEY AUTOINCREMENT,
                                         Nom CHAR(25) NOT NULL UNIQUE,
    FamilleAlimentID INTEGER,
    FOREIGN KEY(FamilleAlimentID) REFERENCES FamilleAliment(FamilleAlimentID),
    UniteID INTEGER,
    FOREIGN KEY(UniteID) REFERENCES Unite(UniteID)
    )

CREATE TABLE IF NOT EXISTS ListeCourse(
    ListeCourseID INTEGER PRIMARY KEY AUTOINCREMENT
)

CREATE TABLE IF NOT EXISTS ListeCourseIngredient(
                                                    ListeCourseID INTEGER,
                                                    FOREIGN KEY(ListeCourseID) REFERENCES ListeCourse(ListeCourseID),
    IngredientID INTEGER,
    FOREIGN KEY(IngredientID) REFERENCES Ingredient(IngredientID),
    Quantite INTEGER NOT NULL
    )

CREATE TABLE IF NOT EXISTS MenuRecette(
                                          Date DATE NOT NULL,
                                          Heure TIME,
                                          RecetteID INTEGER,
                                          FOREIGN KEY(RecetteID) REFERENCES Recette(RecetteID)
    )

drop table Categorie