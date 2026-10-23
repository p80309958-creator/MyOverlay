#!/system/bin/sh

while true; do
    clear
    echo "===================="
    echo "      GoldEg"
    echo "===================="
    echo "1. Проверить ROOT"
    echo "2. Запустить Oxide"
    echo "3. Проверить установку игры"
    echo "0. Выход"
    printf "Выбор: "
    read choice

    case "$choice" in
        1) su -c id ;;
        2) monkey -p com.catsbit.oxidesurvivalisland 1 ;;
        3) su -c 'pm path com.catsbit.oxidesurvivalisland' ;;
        0) exit 0 ;;
        *) echo "Неизвестный пункт" ;;
    esac

    printf "Нажми Enter..."
    read wait
done
