### **ENG**

Smeetup - Meetup Plugin
A fully-featured UHC-style plugin for Minecraft 1.21.4 Paper!
Features

🎯 Game Phase System

**Preparation - players are frozen in cages
Kit Distribution - automatic distribution of customizable kits
Game - teleport to map center and PvP begins
Shrinking Border - creates dynamic gameplay**

📦 Flexible Kit System

**Basic kit for all players (100% chance)
Additional kits with customizable drop chance
Configure number of recipients for each kit
Full customization of items and armor
Enchantment support**

🧱 Automatic Cages

**Automatic construction of 3x3 bedrock cages
Teleport players to individual cages
Remove cages after game starts
Customizable material and size**

🌍 Map System

**Create and save maps using Wand (golden axe)
Set map center and lobby spawn point
Configure regions for maps
Multiple map support**

🎖️ Winner Rewards

**Customizable commands on victory
Can be disabled in config
Supports %player% placeholder**

⚙️ Full Customization

**Duration of each phase
Border parameters (size, speed, damage)
Minimum player count
Game rules (hunger, regeneration, keep inventory)
All messages in separate config file**

📝 Commands

Main Commands

**/sm start - Start the game
/sm forcestop - Force stop the game
/sm reload - Reload configuration**

Map Management

**/sm wand - Get selection tool (golden axe)
/sm newmap <name> - Create a new map
/sm setcenter - Set map center
/sm setlobby - Set lobby spawn point**

Cage Management

**/sm setcolb - Create cage at current location
/sm delcolb <id> - Delete cage by ID
/sm listcolbs - Show list of all cages**

Kit Management

**/sm kit create <name> - Create kit from current inventory
/sm kit delete <name> - Delete kit
/sm kit chance <kit> <chance> - Set drop chance (0-100%)
/sm kit receive <kit> <amount> - Set number of recipients
/sm kit list - List all kits
/sm kit preview <name> - Preview kit contents
/sm kit give <player> <kit> - Give kit to player manually**

📊 PlaceholderAPI

**Plugin supports PlaceholderAPI with the following placeholders:
%meetup_phase% - Current game phase
%meetup_next_phase% - Next phase
%meetup_phasetime% - Time until current phase ends (min:sec)
%meetup_players% - Number of alive players
%meetup_border_size% - Current border size
%meetup_winner% - Last winner
%meetup_status% - Game status (Active/Inactive)**


### **RU**

Smeetup - Meetup Plugin
Полнофункциональный плагин в стиле UHC для Minecraft 1.21.4 Paper!

✨ Особенности

🎯 Система фаз игры

**Подготовка - игроки замораживаются в колбах
Выдача китов - автоматическая раздача настраиваемых наборов
Игра - телепорт в центр карты и начало PvP
Сужающийся бордер - создаёт динамичный геймплей**

📦 Гибкая система китов

**Базовый кит для всех игроков (100% шанс)
Дополнительные киты с настраиваемым шансом выпадения
Настройка количества получателей для каждого кита
Полная кастомизация предметов и брони
Поддержка зачарований**

🧱 Автоматические колбы

**Автоматическое построение бедроковых колб 3x3
Телепортация игроков в отдельные колбы
Удаление колб после начала игры
Настраиваемый материал и размер**

🌍 Система карт

**Создание и сохранение карт через Wand (золотой топор)
Установка центра карты и точки лобби
Настройка регионов для карт
Поддержка нескольких карт**

🎖️ Награды победителю

**Настраиваемые команды при победе
Можно отключить в конфиге
Поддержка плейсхолдера %player%**

⚙️ Полная настройка

**Длительность каждой фазы
Параметры бордера (размер, скорость, урон)
Минимальное количество игроков
Правила игры (голод, регенерация, сохранение вещей)
Все сообщения в отдельном конфиге**

📝 Команды

Основные

**/sm start - Запустить игру
/sm forcestop - Принудительно остановить игру
/sm reload - Перезагрузить конфигурацию

Управление картой

/sm wand - Получить инструмент выделения (золотой топор)
/sm newmap <название> - Создать новую карту
/sm setcenter - Установить центр карты
/sm setlobby - Установить точку лобби

Управление колбами

/sm setcolb - Создать колбу в текущем месте
/sm delcolb <id> - Удалить колбу по ID
/sm listcolbs - Показать список всех колб

Управление китами

/sm kit create <название> - Создать кит из текущего инвентаря
/sm kit delete <название> - Удалить кит
/sm kit chance <кит> <шанс> - Установить шанс выпадения (0-100%)
/sm kit receive <кит> <количество> - Установить количество получателей
/sm kit list - Список всех китов
/sm kit preview <название> - Просмотр содержимого кита
/sm kit give <игрок> <кит> - Выдать кит игроку вручную**

📊 PlaceholderAPI
Плагин поддерживает PlaceholderAPI со следующими плейсхолдерами:

**%meetup_phase% - Текущая фаза игры
%meetup_next_phase% - Следующая фаза
%meetup_phasetime% - Время до конца текущей фазы (мин:сек)
%meetup_players% - Количество живых игроков
%meetup_border_size% - Текущий размер бордера
%meetup_winner% - Последний победитель
%meetup_status% - Статус игры (Активна/Неактивна)**
