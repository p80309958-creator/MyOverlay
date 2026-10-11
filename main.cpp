#include <iostream>

int main() {
    bool boxes = true;
    bool names = true;
    int choice = -1;

    while (choice != 0) {
        std::cout << "\n=== MY ANDROID MENU ===\n";
        std::cout << "1. Boxes: " << (boxes ? "ON" : "OFF") << '\n';
        std::cout << "2. Names: " << (names ? "ON" : "OFF") << '\n';
        std::cout << "0. Exit\n";
        std::cout << "Choose: ";

        if (!(std::cin >> choice)) {
            std::cin.clear();
            std::cin.ignore(10000, '\n');
            continue;
        }

        if (choice == 1) boxes = !boxes;
        if (choice == 2) names = !names;
    }

    return 0;
}
