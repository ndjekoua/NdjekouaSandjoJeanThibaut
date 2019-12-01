-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Giu 13, 2019 alle 15:16
-- Versione del server: 10.1.40-MariaDB
-- Versione PHP: 7.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `s256770`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `seats`
--

DROP TABLE IF EXISTS `seats`;
CREATE TABLE `seats` (
  `id` varchar(11) NOT NULL,
  `line` int(11) NOT NULL,
  `colonna` int(11) NOT NULL,
  `status` varchar(25) NOT NULL,
  `userId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Svuota la tabella prima dell'inserimento `seats`
--

TRUNCATE TABLE `seats`;
--
-- Dump dei dati per la tabella `seats`
--

INSERT DELAYED INTO `seats` (`id`, `line`, `colonna`, `status`, `userId`) VALUES
('2_2', 2, 2, 'sold', 2),
('3_2', 3, 2, 'sold', 2),
('4_1', 4, 1, 'reserved', 1),
('4_2', 4, 2, 'sold', 2),
('4_4', 4, 4, 'reserved', 1),
('4_6', 4, 6, 'reserved', 2);

-- --------------------------------------------------------

--
-- Struttura della tabella `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `userName` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Svuota la tabella prima dell'inserimento `users`
--

TRUNCATE TABLE `users`;
--
-- Dump dei dati per la tabella `users`
--

INSERT DELAYED INTO `users` (`id`, `userName`, `password`) VALUES
(1, 'u1@p.it', 'ec6ef230f1828039ee794566b9c58adc'),
(2, 'u2@p.it', '1d665b9b1467944c128a5575119d1cfd');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `seats`
--
ALTER TABLE `seats`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `userName` (`userName`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
