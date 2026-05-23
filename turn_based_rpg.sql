-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 23, 2026 at 07:25 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `turn_based_rpg`
--

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `ID` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `item_type` enum('HEALTH','MANA','BUFF') NOT NULL,
  `tier` enum('SMALL','MEDIUM','HIGH') NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1,
  `update_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

CREATE TABLE `players` (
  `ID` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(16) NOT NULL,
  `role` enum('WARRIOR','MAGE','ARCHER') NOT NULL,
  `level` int(11) NOT NULL DEFAULT 1,
  `exp` int(11) NOT NULL,
  `gold` int(11) NOT NULL,
  `floor` int(11) NOT NULL,
  `highest_cleared_floor` int(11) NOT NULL DEFAULT 0,
  `current_hp` int(11) NOT NULL,
  `current_mp` int(11) NOT NULL,
  `update_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `stat_hp` int(11) NOT NULL,
  `stat_atk` int(11) NOT NULL,
  `stat_def` int(11) NOT NULL,
  `stat_magic` int(11) NOT NULL,
  `stat_mana` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_skills`
--

CREATE TABLE `player_skills` (
  `iD` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `skill_name` varchar(255) NOT NULL,
  `update_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `playe_inventory` (`player_id`);

--
-- Indexes for table `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `player_skills`
--
ALTER TABLE `player_skills`
  ADD PRIMARY KEY (`iD`),
  ADD KEY `player_skills` (`player_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `inventory`
--
ALTER TABLE `inventory`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `players`
--
ALTER TABLE `players`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `player_skills`
--
ALTER TABLE `player_skills`
  MODIFY `iD` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `inventory`
--
ALTER TABLE `inventory`
  ADD CONSTRAINT `playe_inventory` FOREIGN KEY (`player_id`) REFERENCES `players` (`ID`);

--
-- Constraints for table `player_skills`
--
ALTER TABLE `player_skills`
  ADD CONSTRAINT `id_user` FOREIGN KEY (`player_id`) REFERENCES `players` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
