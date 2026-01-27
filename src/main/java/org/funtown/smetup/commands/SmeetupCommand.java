package org.funtown.smetup.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.funtown.smetup.Smeetup;
import org.funtown.smetup.models.Colb;
import org.funtown.smetup.models.GameMap;
import org.funtown.smetup.models.Kit;
import org.funtown.smetup.utils.MessageUtil;
import java.util.Arrays;

public class SmeetupCommand implements CommandExecutor {
    private final Smeetup plugin;

    public SmeetupCommand(Smeetup plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("smeetup.admin")) {
            sender.sendMessage(MessageUtil.colorize("&cУ вас нет прав!"));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "start":
                handleStart(sender);
                break;
            case "forcestop":
                handleForceStop(sender);
                break;
            case "reload":
                handleReload(sender);
                break;
            case "setcenter":
                handleSetCenter(sender);
                break;
            case "setlobby":
                handleSetLobby(sender);
                break;
            case "setcolb":
                handleSetColb(sender);
                break;
            case "delcolb":
                handleDelColb(sender, args);
                break;
            case "listcolbs":
                handleListColbs(sender);
                break;
            case "wand":
                handleWand(sender);
                break;
            case "newmap":
                handleNewMap(sender, args);
                break;
            case "kit":
                handleKit(sender, args);
                break;
            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void handleStart(CommandSender sender) {
        if (plugin.getGameManager().startGame()) {
            sender.sendMessage(MessageUtil.colorize("&aИгра запущена!"));
        } else {
            int minPlayers = plugin.getConfigManager().getConfig("config").getInt("game.min-players");
            sender.sendMessage(MessageUtil.colorize("&cНедостаточно игроков! Нужно: " + minPlayers));
        }
    }

    private void handleForceStop(CommandSender sender) {
        plugin.getGameManager().forceStop();
        sender.sendMessage(MessageUtil.colorize("&aИгра остановлена!"));
    }

    private void handleReload(CommandSender sender) {
        plugin.getConfigManager().reloadConfigs();
        plugin.getColbManager().loadColbs();
        plugin.getKitManager().loadKits();
        plugin.getMapManager().loadMaps();
        sender.sendMessage(MessageUtil.colorize("&aКонфиги перезагружены!"));
    }

    private void handleSetCenter(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.colorize("&cТолько для игроков!"));
            return;
        }

        Player player = (Player) sender;
        GameMap map = plugin.getMapManager().getCurrentMap();

        if (map == null) {
            sender.sendMessage(MessageUtil.colorize("&cНет активной карты! Создайте карту командой /sm newmap <название>"));
            return;
        }

        map.setCenter(player.getLocation());
        plugin.getMapManager().saveMaps();
        sender.sendMessage(MessageUtil.colorize("&aЦентр карты установлен!"));
    }

    private void handleSetLobby(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.colorize("&cТолько для игроков!"));
            return;
        }

        Player player = (Player) sender;
        GameMap map = plugin.getMapManager().getCurrentMap();

        if (map == null) {
            sender.sendMessage(MessageUtil.colorize("&cНет активной карты!"));
            return;
        }

        map.setLobby(player.getLocation());
        plugin.getMapManager().saveMaps();
        sender.sendMessage(MessageUtil.colorize("&aЛобби установлено!"));
    }

    private void handleSetColb(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.colorize("&cТолько для игроков!"));
            return;
        }

        Player player = (Player) sender;
        Colb colb = plugin.getColbManager().addColb(player.getLocation());
        sender.sendMessage(MessageUtil.colorize("&aКолба #" + colb.getId() + " создана!"));
    }

    private void handleDelColb(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(MessageUtil.colorize("&cИспользование: /sm delcolb <id>"));
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);
            if (plugin.getColbManager().removeColb(id)) {
                sender.sendMessage(MessageUtil.colorize("&aКолба #" + id + " удалена!"));
            } else {
                sender.sendMessage(MessageUtil.colorize("&cКолба не найдена!"));
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageUtil.colorize("&cНеверный ID!"));
        }
    }

    private void handleListColbs(CommandSender sender) {
        int count = plugin.getColbManager().getColbCount();
        sender.sendMessage(MessageUtil.colorize("&eВсего колб: &a" + count));

        for (Colb colb : plugin.getColbManager().getColbs()) {
            Location loc = colb.getLocation();
            sender.sendMessage(MessageUtil.colorize(
                    "&7#" + colb.getId() + " &8- &e" +
                            loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ()
            ));
        }
    }

    private void handleWand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.colorize("&cТолько для игроков!"));
            return;
        }

        Player player = (Player) sender;
        ItemStack wand = new ItemStack(Material.GOLDEN_AXE);
        ItemMeta meta = wand.getItemMeta();
        meta.setDisplayName(MessageUtil.colorize("&6Smeetup Wand"));
        meta.setLore(Arrays.asList(
                MessageUtil.colorize("&7ЛКМ - Установить точку 1"),
                MessageUtil.colorize("&7ПКМ - Установить точку 2")
        ));
        wand.setItemMeta(meta);

        player.getInventory().addItem(wand);
        sender.sendMessage(MessageUtil.colorize("&aЗолотой топор (Wand) выдан!"));
    }

    private void handleNewMap(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(MessageUtil.colorize("&cИспользование: /sm newmap <название>"));
            return;
        }

        String mapName = args[1];
        GameMap map = plugin.getMapManager().createMap(mapName);

        if (map != null) {
            sender.sendMessage(MessageUtil.colorize("&aКарта '" + mapName + "' создана и установлена как текущая!"));
            sender.sendMessage(MessageUtil.colorize("&eТеперь установите центр: /sm setcenter"));
            sender.sendMessage(MessageUtil.colorize("&eИ лобби: /sm setlobby"));
        } else {
            sender.sendMessage(MessageUtil.colorize("&cСначала выделите регион с помощью /sm wand!"));
        }
    }

    private void handleKit(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(MessageUtil.colorize("&cИспользование: /sm kit <create|delete|chance|receive|list|preview|give>"));
            return;
        }

        String action = args[1].toLowerCase();

        switch (action) {
            case "create":
                handleKitCreate(sender, args);
                break;
            case "delete":
                handleKitDelete(sender, args);
                break;
            case "chance":
                handleKitChance(sender, args);
                break;
            case "receive":
                handleKitReceive(sender, args);
                break;
            case "list":
                handleKitList(sender);
                break;
            case "preview":
                handleKitPreview(sender, args);
                break;
            case "give":
                handleKitGive(sender, args);
                break;
            default:
                sender.sendMessage(MessageUtil.colorize("&cНеизвестная подкоманда!"));
                break;
        }
    }

    private void handleKitCreate(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.colorize("&cТолько для игроков!"));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(MessageUtil.colorize("&cИспользование: /sm kit create <название>"));
            return;
        }

        Player player = (Player) sender;
        String kitName = args[2];

        Kit kit = plugin.getKitManager().createKit(kitName, player);
        sender.sendMessage(MessageUtil.colorize("&aКит '" + kitName + "' создан!"));
    }

    private void handleKitDelete(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(MessageUtil.colorize("&cИспользование: /sm kit delete <название>"));
            return;
        }

        String kitName = args[2];

        if (plugin.getKitManager().deleteKit(kitName)) {
            sender.sendMessage(MessageUtil.colorize("&aКит '" + kitName + "' удален!"));
        } else {
            sender.sendMessage(MessageUtil.colorize("&cКит не найден!"));
        }
    }

    private void handleKitChance(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(MessageUtil.colorize("&cИспользование: /sm kit chance <название> <шанс>"));
            return;
        }

        String kitName = args[2];
        Kit kit = plugin.getKitManager().getKit(kitName);

        if (kit == null) {
            sender.sendMessage(MessageUtil.colorize("&cКит не найден!"));
            return;
        }

        try {
            int chance = Integer.parseInt(args[3]);
            kit.setChance(chance);
            plugin.getKitManager().saveKits();
            sender.sendMessage(MessageUtil.colorize("&aШанс кита '" + kitName + "' установлен: " + chance + "%"));
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageUtil.colorize("&cНеверный шанс!"));
        }
    }

    private void handleKitReceive(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(MessageUtil.colorize("&cИспользование: /sm kit receive <название> <количество>"));
            return;
        }

        String kitName = args[2];
        Kit kit = plugin.getKitManager().getKit(kitName);

        if (kit == null) {
            sender.sendMessage(MessageUtil.colorize("&cКит не найден!"));
            return;
        }

        try {
            int receivers = Integer.parseInt(args[3]);
            kit.setReceivers(receivers);
            plugin.getKitManager().saveKits();
            sender.sendMessage(MessageUtil.colorize("&aКоличество получателей кита '" + kitName + "': " + receivers));
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageUtil.colorize("&cНеверное количество!"));
        }
    }

    private void handleKitList(CommandSender sender) {
        sender.sendMessage(MessageUtil.colorize("&eСписок китов:"));
        for (Kit kit : plugin.getKitManager().getAllKits()) {
            sender.sendMessage(MessageUtil.colorize(
                    "&7- &e" + kit.getName() +
                            " &8(&aШанс: " + kit.getChance() + "%" +
                            ", Получат: " + (kit.getReceivers() == -1 ? "все" : kit.getReceivers()) + "&8)"
            ));
        }
    }

    private void handleKitPreview(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(MessageUtil.colorize("&cИспользование: /sm kit preview <название>"));
            return;
        }

        String kitName = args[2];
        Kit kit = plugin.getKitManager().getKit(kitName);

        if (kit == null) {
            sender.sendMessage(MessageUtil.colorize("&cКит не найден!"));
            return;
        }

        sender.sendMessage(MessageUtil.colorize("&eПредметы кита '" + kitName + "':"));
        for (ItemStack item : kit.getItems()) {
            sender.sendMessage(MessageUtil.colorize("&7- &e" + item.getType() + " x" + item.getAmount()));
        }
    }

    private void handleKitGive(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(MessageUtil.colorize("&cИспользование: /sm kit give <игрок> <кит>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage(MessageUtil.colorize("&cИгрок не найден!"));
            return;
        }

        String kitName = args[3];
        Kit kit = plugin.getKitManager().getKit(kitName);

        if (kit == null) {
            sender.sendMessage(MessageUtil.colorize("&cКит не найден!"));
            return;
        }

        plugin.getKitManager().giveKit(target, kit);
        sender.sendMessage(MessageUtil.colorize("&aКит выдан игроку " + target.getName()));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(MessageUtil.colorize("&6&l=== Smeetup Commands ==="));
        sender.sendMessage(MessageUtil.colorize("&e/sm start &7- Начать игру"));
        sender.sendMessage(MessageUtil.colorize("&e/sm forcestop &7- Остановить игру"));
        sender.sendMessage(MessageUtil.colorize("&e/sm reload &7- Перезагрузить конфиги"));
        sender.sendMessage(MessageUtil.colorize("&e/sm setcenter &7- Установить центр карты"));
        sender.sendMessage(MessageUtil.colorize("&e/sm setlobby &7- Установить лобби"));
        sender.sendMessage(MessageUtil.colorize("&e/sm setcolb &7- Создать колбу"));
        sender.sendMessage(MessageUtil.colorize("&e/sm delcolb <id> &7- Удалить колбу"));
        sender.sendMessage(MessageUtil.colorize("&e/sm listcolbs &7- Список колб"));
        sender.sendMessage(MessageUtil.colorize("&e/sm wand &7- Получить золотой топор"));
        sender.sendMessage(MessageUtil.colorize("&e/sm newmap <название> &7- Создать карту"));
        sender.sendMessage(MessageUtil.colorize("&e/sm kit create <название> &7- Создать кит"));
        sender.sendMessage(MessageUtil.colorize("&e/sm kit delete <название> &7- Удалить кит"));
        sender.sendMessage(MessageUtil.colorize("&e/sm kit chance <кит> <шанс> &7- Установить шанс"));
        sender.sendMessage(MessageUtil.colorize("&e/sm kit receive <кит> <кол-во> &7- Кол-во получателей"));
        sender.sendMessage(MessageUtil.colorize("&e/sm kit list &7- Список китов"));
        sender.sendMessage(MessageUtil.colorize("&e/sm kit preview <название> &7- Просмотр кита"));
        sender.sendMessage(MessageUtil.colorize("&e/sm kit give <игрок> <кит> &7- Выдать кит"));
    }
}