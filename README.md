# Muzikos Redaktorius

**Autorius**: Eigintas Urbanavičius <br>
**Data**: 2025-05-27

---

## Paskirtis

Ši programa yra JavaFX pagrįsta darbalaukio programa, skirta muzikos kūriniams redaguoti. Ji leidžia vartotojams tvarkyti muzikines natas (MIDI pagrindu), keisti metaduomenis (pavadinimą, autorių, fragmentus), manipuliuoti garso duomenimis arba natomis, groti kūrinius ir išsaugoti/įkelti projektus _.med_ formatu.

## Paleidimas

##### Reikalavimai

- Java 17
- JavaFX SDK 17
- Gradle 8.0+

##### Instaliavimas

1. Klonuokite saugyklą:

```
git clone https://github.com/Tlajja/MusicEditor.git
```

2. Eikite į projekto saugyklą:

```
cd MusicEditor
```

3. Sukurkite projektą:

```
./gradlew build
```

4. Paleiskite projektą:

```
./gradlew run
```

## Funkcionalumas

- **MP3 redagavimas:**

  - Keisti pavadinimą, autorių ir fragmentus.
    <br><br>

- **Natų redagavimas:**

  - Redaguoti muzikines natas (pvz., C4, F#5) su stiprumu ir trukme.

  - Nustatyti tonaciją, tempą ir MIDI instrumentą.

  - Groti natas per MIDI.

- **Vartotojo sąsaja:**

  - JavaFX pagrįsta sąsaja su įrankių juosta, formomis ir sąrašais.

  - Perjungimas tarp MP3 ir natų redaktorių.

- **Failų operacijos:**

  - Išsaugoti/įkelti kūrinius .med formatu.<br><br>

- **Dokumentacija:**

  - Javadoc prieinamas _app/build/docs/javadoc/index.html._

---

## Pagrindinės klasės

- **Base.MusicEditor**: Abstrakti bazinė klasė redaktoriams.
- **Base.MP3Editor**: MP3 formato kūrinių redagavimo logika.
- **Base.NotationEditor**: Natų redagavimo logika.
- **App.MusicEditorApp**: (JavaFX) programo įėjimo taškas.
- **App.EditorUI**: Vartotojo sąsajos valdymas/logika.
- **App.EditorManager**: Redaktorių koordinavimas.
- **Advanced.Factory.MusicEditorFactory**: Redaktorių kūrimo gamykla.

## Projektavimo šablonai

Programoje panaudoti šie projektavimo šablonai (design patterns):

1. **Factory Method** (Gamyklos metodas) - naudojamas `MusicEditorFactory`, `MP3EditorFactory` ir `NotationEditorFactory` klasėse redaktorių objektų sukūrimui. Šis šablonas leidžia lengvai kurti skirtingų tipų redaktorius.

2. **Template Method** (Šablono metodas) - `Base.MusicEditor` apibrėžia bendras operacijas (`modify`, `cut`), o abstraktų metodą `play` įgyvendina `MP3Editor` ir `NotationEditor`.

3. **Strategy** (Strategija)- `App.EditorManager` naudoja `EditorType` enumą perjungti tarp `MP3Editor` ir `NotationEditor` strategijų vykdymo metu.

4. **Observer** (Stebėtojas) - `App.EditorUI` naudoja JavaFX įvykių tvarkykles (pvz., `setOnAction`), kad reaguotų į vartotojo sąsajos pokyčius.

## Plėtimo galimybės

Programą galima plėsti šiomis kryptimis:

- Įdiegti papildomus redaktorius (pvz. TabsEditor, WavEditor etc.)
- Įgyvendinti fragments (Intro, Chorus, Verse) funkcionalumą, kad galima būtų redaguoti tik juos ir tada juos apjungti, panaikinti ir kt.
- Sukurti galimybe groti per daugiau kanalų t.y. kad vienu metu grotų 2 ar daugiau instrumentų.
- Pridėti daugiau instrumentų pasirinkimų.
- Sukurti lengvesnį būdą pridėti natas/akordus.
- Pridėti MP3 redaktoriaus grojimo funkcionalumą.
- ...

## Klasių diagrama

![Image](ClassDiagram.png)
