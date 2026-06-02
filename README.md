# ⚔️ Dungeon Realm — Turn-Based RPG

Game RPG berbasis giliran (turn-based) yang dibuat menggunakan Java Swing, dengan tampilan konsol bergaya dark fantasy. Pemain menjelajahi dungeon berlantai, bertarung menggunakan skill dan item, serta memiliki sistem karakter yang terus berkembang dengan penyimpanan data persisten ke MySQL.

---

## 📋 Daftar Isi

- [Fitur](#-fitur)
- [Teknologi](#-teknologi)
- [Arsitektur MVC](#-arsitektur-mvc)
- [Struktur Proyek](#-struktur-proyek)
- [Cara Menjalankan](#-cara-menjalankan)
- [Setup Database](#-setup-database)
- [Cara Bermain](#-cara-bermain)
- [Kelas & Skill](#-kelas--skill)
- [Sistem Dungeon](#-sistem-dungeon)

---

## ✨ Fitur

- **Pertarungan turn-based** — serang, bertahan, gunakan skill, gunakan item, atau kabur
- **3 kelas karakter** — Warrior, Mage, Archer, masing-masing dengan stat awal dan bonus level-up yang berbeda
- **Sistem skill** — 14 skill tersedia (serangan, penyembuhan, pertahanan, buff); 1 skill baru terbuka setiap 10 lantai
- **Sistem buff** — peningkatan stat sementara (Attack, Defense, Magic, Mana) dengan hitungan durasi per giliran
- **Sistem inventory** — potion Health, Mana, dan Revive dalam tier Small/Medium/Large; slot bisa ditumpuk (stackable); bisa dipakai dalam dan luar pertarungan
- **Sistem dungeon** — 100 lantai dengan 4 level kesulitan, 5 wave per lantai biasa, boss fight setiap 10 lantai
- **Replay lantai** — ulangi lantai yang sudah pernah diselesaikan (dengan batasan level)
- **Penyimpanan otomatis** — seluruh data pemain, inventory, dan skill tersimpan ke MySQL secara otomatis
- **UI dark fantasy** — tema Swing custom dengan aksen emas/ember, stat bar bergradasi, dan battle log berwarna

---

## 🛠 Teknologi

| Komponen | Teknologi |
|---|---|
| Bahasa | Java 22 |
| UI Framework | Java Swing |
| Database | MySQL / MariaDB |
| Driver DB | mysql-connector-j 8.3.0 |
| Build Tool | Apache Maven |

---

## 🏗 Arsitektur MVC

Proyek ini menggunakan pola **MVC (Model-View-Controller)** dengan pemisahan tanggung jawab yang jelas:

```
View  ──►  Controller  ──►  Service  ──►  Model
                │                           │
                └────────────►  DAO  ───────┘
```

| Layer | Package | Tanggung Jawab |
|---|---|---|
| **Model** | `model/` | Data domain — karakter, stat, skill, item, buff, enum |
| **View** | `view/` | Panel Swing — tidak mengakses field model secara langsung |
| **Controller** | `game/controller/` | Jembatan antara View dan Service; mengekspos snapshot record ke View |
| **Service** | `game/services/` | Logika permainan yang melibatkan banyak model (battle engine, dungeon loop) |
| **DAO** | `database/dao/` | Seluruh operasi baca/tulis ke database |

Controller mengekspos **snapshot record** yang immutable (contoh: `PlayerBattleSnapshot`, `PlayerStatusSnapshot`) ke View, sehingga lapisan UI tidak pernah memegang referensi langsung ke objek model.

---

## 📁 Struktur Proyek

```
src/main/java/kelompok11/turnbaserpg/
│
├── Main.java
│
├── database/
│   ├── Connector.java               ← koneksi ke MySQL
│   └── dao/
│       ├── InventoryDAO.java
│       ├── PlayerDAO.java
│       └── SkillsDAO.java
│
├── game/
│   ├── controller/
│   │   ├── BattleController.java    ← alur pertarungan
│   │   ├── BattleViewController.java← snapshot HUD pertarungan
│   │   ├── DungeonController.java   ← alur dungeon per lantai
│   │   ├── GameController.java      ← controller utama (login, register, state)
│   │   ├── LoadManager.java         ← load data pemain dari DB
│   │   ├── MainMenuController.java  ← logika main menu & dialog
│   │   └── SaveManager.java         ← simpan data pemain ke DB
│   │
│   └── services/
│       ├── BattleEvent.java         ← event hasil aksi pertarungan
│       ├── BattleService.java       ← engine logika pertarungan
│       ├── DungeonEvent.java        ← event progres dungeon
│       └── DungeonService.java      ← engine logika dungeon
│
├── model/
│   ├── Character/
│   │   ├── Character.java           ← abstract base karakter
│   │   ├── Enemy.java
│   │   ├── Inventory.java
│   │   ├── InventorySlot.java
│   │   ├── Player.java
│   │   └── Stats.java
│   │
│   ├── buff/
│   │   ├── Buff.java                ← abstract base buff
│   │   ├── AttackBuff.java
│   │   ├── DefenseBuff.java
│   │   ├── MagicBuff.java
│   │   └── ManaBuff.java
│   │
│   ├── enums/
│   │   ├── BattleResult.java
│   │   ├── BuffType.java
│   │   ├── ConsumableType.java
│   │   ├── Difficulty.java
│   │   ├── PotionTier.java
│   │   ├── Role.java
│   │   └── SkillType.java
│   │
│   ├── item/
│   │   ├── Item.java                ← abstract base item
│   │   ├── Usable.java              ← interface untuk item yang bisa dipakai
│   │   └── consumable/
│   │       ├── Potion.java          ← abstract base potion
│   │       ├── HealthPotion.java
│   │       ├── ManaPotion.java
│   │       └── RevivePotion.java
│   │
│   └── skill/
│       ├── Skill.java               ← abstract base skill
│       ├── BasicHeal.java           ├── GreaterHeal.java
│       ├── LifeDrain.java           ├── FireBall.java
│       ├── IceSpear.java            ├── ThunderStrike.java
│       ├── ShadowSlash.java         ├── EarthCrusher.java
│       ├── DragonFury.java          ├── IronWall.java
│       ├── StoneBody.java           ├── GuardianAura.java
│       ├── BerserkerRage.java       └── ArcanePower.java
│
├── utils/
│   ├── GameConstants.java           ← seluruh konstanta & konfigurasi game
│   └── GameLogger.java              ← logger ke file logs/game.log
│
└── view/
    ├── GameFrame.java               ← jendela utama + navigasi CardLayout
    ├── LoginPanel.java              ← layar login & register
    ├── MainMenuPanel.java           ← main menu + dialog inventory/skill/stat
    ├── DungeonPanel.java            ← tampilan dungeon + battle loop
    ├── BattlePanel.java             ← tampilan pertarungan
    ├── RPGComponents.java           ← komponen Swing reusable (button, stat bar, dll)
    └── RPGTheme.java                ← konstanta tema (warna, font, border)
```

---

## 🚀 Cara Menjalankan

### Prasyarat

- Java 22 atau lebih baru
- Maven 3.6 atau lebih baru
- MySQL / MariaDB berjalan di lokal

### 1. Clone repositori

```bash
git clone https://github.com/username-kamu/SimpleTurnBaseRPG.git
cd SimpleTurnBaseRPG
```

### 2. Setup database

Lihat bagian [Setup Database](#-setup-database) di bawah.

### 3. Sesuaikan konfigurasi koneksi

Buka `src/main/java/kelompok11/turnbaserpg/database/Connector.java` dan sesuaikan jika kredensial MySQL kamu berbeda:

```java
String url      = "jdbc:mysql://localhost:3306/turn_based_rpg";
String user     = "root";
String password = "";
```

### 4. Build dan jalankan

```bash
mvn compile
mvn exec:java
```

---

## 🗄 Setup Database

Import file SQL yang sudah disediakan untuk membuat database dan seluruh tabelnya:

```bash
mysql -u root -p < turn_based_rpg.sql
```

Atau jalankan file tersebut melalui MySQL Workbench / phpMyAdmin. Script ini akan membuat database `turn_based_rpg` dengan tiga tabel:

| Tabel | Keterangan |
|---|---|
| `players` | Stat karakter, level, EXP, gold, dan progres lantai |
| `inventory` | Slot item per pemain beserta tipe, tier, dan jumlahnya |
| `player_skills` | Daftar nama skill yang sudah di-unlock per pemain |

**Relasi antar tabel:**

```
players (ID) ──┬──► inventory    (player_id)
               └──► player_skills (player_id)
```

---

## 🎮 Cara Bermain

### Login & Register

Saat pertama kali dibuka, layar Login/Register akan muncul. Pemain baru memilih kelas karakter (Warrior, Mage, atau Archer) dan langsung tersimpan ke database.

### Main Menu

Dari main menu kamu bisa:

| Menu | Keterangan |
|---|---|
| **Enter Dungeon** | Lanjutkan dari lantai terakhir |
| **Replay Floor** | Ulangi lantai yang sudah pernah diselesaikan |
| **Inventory** | Lihat dan gunakan potion di luar pertarungan |
| **Skills** | Lihat semua skill yang sudah di-unlock beserta mana cost dan cooldown |
| **Character Stats** | Lihat detail stat lengkap termasuk efek buff aktif |

### Pertarungan

Setiap putaran pertarungan berjalan seperti ini:

**Giliran Pemain — pilih satu aksi:**

| Aksi | Keterangan |
|---|---|
| `Basic Attack` | Serangan fisik (Warrior/Archer) atau sihir (Mage) |
| `Defend` | Tambah bonus DEF sementara hingga serangan musuh berikutnya |
| `Use Skill` | Keluarkan skill dengan biaya mana (tergantung cooldown) |
| `Use Item` | Konsumsi potion dari inventory |
| `Escape` | Coba kabur — peluang berhasil 50% |

**Giliran Musuh:** menggunakan skill attack setiap 3 giliran, serangan biasa di giliran lainnya.

Menang pertarungan memberikan EXP (bertingkat sesuai level), peluang 30% mendapat gold, dan peluang 40% mendapat loot berupa potion.

---

## 🧙 Kelas & Skill

### Kelas Karakter

| Kelas | HP | ATK | DEF | Magic | Mana | Keunggulan |
|---|---|---|---|---|---|---|
| **Warrior** | 120 | 15 | 30 | 5 | 30 | Tank, DEF tinggi |
| **Mage** | 70 | 10 | 5 | 30 | 70 | Magic damage tinggi |
| **Archer** | 90 | 25 | 8 | 10 | 50 | ATK fisik tinggi |

Warrior dan Archer memakai ATK untuk serangan dasar; Mage memakai Magic.

**Bonus saat level up:**

| Kelas | Bonus per Level |
|---|---|
| Warrior | +30 HP, +10 DEF |
| Mage | +5 Magic, +10 Mana |
| Archer | +8 ATK, +3 DEF, +10 HP |

### Daftar Skill

Skill `Basic Heal` otomatis diberikan saat karakter dibuat. Skill baru terbuka setiap 10 lantai hingga maksimal 11 skill.

| Skill | Tipe | Effect | Mana | Cooldown |
|---|---|---|---|---|
| Basic Heal | Heal | +25 HP | 20 | 1 giliran |
| Greater Heal | Heal | +150 HP | 35 | 3 giliran |
| Life Drain | Heal | +35 HP (serap dari musuh) | 20 | 2 giliran |
| Fire Ball | Attack | 15 + Magic damage | 10 | 2 giliran |
| Ice Spear | Attack | 40 + Magic damage | 18 | 2 giliran |
| Thunder Strike | Attack | 45 + Magic damage | 20 | 3 giliran |
| Shadow Slash | Attack | 30 + Magic damage | 10 | 1 giliran |
| Earth Crusher | Attack | 55 + Magic damage | 30 | 3 giliran |
| Dragon Fury | Attack | 90 + Magic damage | 40 | 5 giliran |
| Iron Wall | Defend | +20 DEF buff (3 giliran) | 10 | 2 giliran |
| Stone Body | Defend | +20 DEF buff (3 giliran) | 10 | 1 giliran |
| Guardian Aura | Defend | +30 DEF buff (3 giliran) | 20 | 2 giliran |
| Berserker Rage | Buff | +25 ATK buff (3 giliran) | 15 | 2 giliran |
| Arcane Power | Buff | +30 Magic buff (3 giliran) | 20 | 2 giliran |

---

## 🏰 Sistem Dungeon

| Parameter | Nilai |
|---|---|
| Total lantai | 100 |
| Wave per lantai biasa | 5 |
| Boss floor setiap | 10 lantai |
| Multiplier HP boss | 3× |
| Multiplier ATK boss | 2× |
| Max level pemain | 100 |
| Interval unlock skill | Setiap 10 lantai |
| Max slot inventory | 30 |
| Max skill aktif | 11 |

### Skala Kesulitan

| Rentang Lantai | Kesulitan | Multiplier Stat Musuh |
|---|---|---|
| 1 – 10 | Easy | 1.0× |
| 11 – 30 | Normal | 1.2× |
| 31 – 60 | Hard | 1.3× |
| 61 – 100 | Nightmare | 1.5× |

Stat musuh juga bertambah secara linear sesuai nomor lantai di atas multiplier kesulitan, sehingga setiap lantai selalu menawarkan tantangan yang lebih berat.

### Jenis Musuh

| Kesulitan | Musuh Biasa | Boss |
|---|---|---|
| Easy | Goblin, Slime, Wolf | Goblin King, Slime King, Alpha Wolf King |
| Normal | Troll, Orc, Skeleton | Orc Warlord, Stone Golem |
| Hard | Demon, Succubus, Vampire | Vampire Lord, Arch Demon |
| Nightmare | Dragon, Lich, Dark Knight | Ancient Dragon, Death Lich, Shadow Emperor |

---

## 📝 Logging

Aplikasi mencatat log terstruktur ke file `logs/game.log` melalui `GameLogger`. Level log yang tersedia: `INFO`, `WARNING`, `ERROR`, `DEBUG`. Konsol sengaja dibuat senyap selama bermain normal — semua output diarahkan ke file log.

---

## 👥 Tim Pengembang

Dibuat oleh **Kelompok 11**.
