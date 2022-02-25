CREATE TABLE IF NOT EXISTS FamilleAliment(
                                             FamilleAlimentID INTEGER PRIMARY KEY,
                                             Nom CHAR(20) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS Unite(
                                    UniteID INTEGER PRIMARY KEY,
                                    Nom CHAR(10) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS Categorie(
                                        CategorieID INTEGER PRIMARY KEY,
                                        Nom CHAR(20) NOT NULL UNIQUE
    )

CREATE TABLE IF NOT EXISTS Recette(
                                      RecetteID INTEGER PRIMARY KEY,
                                      Nom CHAR(40) NOT NULL UNIQUE,
    Duree INTEGER NOT NULL,
    NbPersonnes INTEGER NOT NULL,
    TypePlat CHAR(40) NOT NULL UNIQUE,
    Preparation TEXT NOT NULL,
    CategorieID INTEGER,
    FOREIGN KEY(CategorieID) REFERENCES Categorie(CategorieID)
    )

CREATE TABLE IF NOT EXISTS RecetteIngredient(
                                                RecetteID INTEGER NOT NULL ,
                                                FOREIGN KEY(RecetteID) REFERENCES Recette(RecetteID),
    IngredientID INTEGER NOT NULL,
    FOREIGN KEY(IngredientID) REFERENCES Ingredient(IngredientID),
    Quantite INTEGER NOT NULL,
    PRIMARY KEY (RecetteID, IngredientID)
    )

CREATE TABLE IF NOT EXISTS Ingredient(
                                         IngredientID INTEGER PRIMARY KEY,
                                         Nom CHAR(25) NOT NULL UNIQUE,
    FamilleAlimentID INTEGER,
    FOREIGN KEY(FamilleAlimentID) REFERENCES FamilleAliment(FamilleAlimentID),
    UniteID INTEGER,
    FOREIGN KEY(UniteID) REFERENCES Unite(UniteID)
    )
