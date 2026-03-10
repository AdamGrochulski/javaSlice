# javaslice — Graficzny podział grafów (Java)

**javaslice** to aplikacja z graficznym interfejsem użytkownika (GUI) napisana w języku Java (Swing), służąca do wizualizacji i podziału grafów nieskierowanych. Program dąży do równomiernego rozłożenia wierzchołków przy minimalizacji liczby przeciętych krawędzi.

## 🌟 Kluczowe cechy
- **Interfejs GUI:** Zbudowany z użyciem biblioteki Swing, oferuje intuicyjną obsługę i interaktywny podgląd grafu.
- **Wizualizacja:** Możliwość wczytywania, wyświetlania oraz centrowania widoku na konkretnych wierzchołkach.
- **Algorytmy:** Wykorzystuje algorytm **Kernighana-Lina** do optymalizacji podziału.
- **Tryb Performance:** Specjalny tryb wyświetlania umożliwiający przeglądanie bardzo dużych grafów.
- **Personalizacja:** Obsługa motywów kolorystycznych (jasny i ciemny).

## 🚀 Funkcje programu
- **Podziel graf:** Uruchamia proces dzielenia, pozwalając użytkownikowi określić liczbę partycji oraz margines błędu.
- **Lokalizacja wierzchołka:** Szybkie wyszukiwanie i podświetlanie konkretnego węzła w strukturze.
- **Import/Eksport:** Obsługa plików `.csrrg` oraz formatów binarnych.
- **Statystyki:** Wyświetlanie szczegółowych informacji o strukturze grafu.

## 🛠 Struktura klas (Backend)
- `Graph`: Główna klasa reprezentująca strukturę danych grafu.
- `KernighanLin`: Implementacja algorytmu optymalizacji podziału.
- `TextFileImporter` / `BinaryFileImporter`: Moduły odpowiedzialne za wczytywanie danych.
- `Node` / `Edge`: Reprezentacje obiektowe elementów grafu.

## 📋 Wymagania
- Java Runtime Environment (JRE) 8 lub nowsze.
- Biblioteka Swing (standardowo w JDK).

## 📖 Instrukcja obsługi
1. Uruchom aplikację.
2. Z menu **Plik** wybierz opcję wczytywania grafu (`.csrrg` lub `.bin`).
3. Użyj przycisku **Podziel graf**, aby otworzyć okno konfiguracji parametrów (liczba części, margines).
4. Przeglądaj wynik wizualny bezpośrednio w oknie głównym.

## ⚠️ Ograniczenia
- Program zachowuje płynność dla małych i średnich grafów. Przy bardzo dużych strukturach płynność interfejsu może być mniejsza, choć program pozostaje użyteczny dzięki trybowi *Performance*.
- Optymalizacja marginesu błędu może w specyficznych przypadkach nie osiągnąć założonego celu przy bardzo gęstych grafach.

## 👥 Autorzy
- **Adam Grochulski**
- **Przemysław Pindral**
