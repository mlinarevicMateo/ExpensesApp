-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 03, 2018 at 09:57 PM
-- Server version: 10.1.30-MariaDB
-- PHP Version: 7.2.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `expensesapp`
--

-- --------------------------------------------------------

--
-- Table structure for table `balances`
--

CREATE TABLE `balances` (
  `id` int(11) NOT NULL,
  `amount` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `month` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `year` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `balances`
--

INSERT INTO `balances` (`id`, `amount`, `month`, `year`, `user_id`) VALUES
(5, '791.0', 'March', '2018', 2),
(6, '246.0', 'April', '2018', 1),
(7, '5', 'December', '2017', 1),
(15, '5', 'February', '2018', 1),
(16, '2', 'January', '2018', 1),
(30, '2594.43', 'March', '2018', 1),
(31, '100', 'April', '2018', 11);

-- --------------------------------------------------------

--
-- Table structure for table `expenses`
--

CREATE TABLE `expenses` (
  `id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `price` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `date` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `time` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `is_visible` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `expenses`
--

INSERT INTO `expenses` (`id`, `name`, `price`, `date`, `time`, `user_id`, `is_visible`) VALUES
(3, 'asd', '123', '27/March/2018', '20:26:8', 1, 0),
(4, 'Mateo', '123', '27/March/2018', '20:52:29', 1, 1),
(5, 'Mateo', '1000', '27/March/2018', '21:19:55', 1, 1),
(6, 'Novi trosak', '34.45', '27/March/2018', '21:37:48', 1, 1),
(7, 'Novi', '34.45', '27/March/2018', '21:38:51', 1, 1),
(8, 'Najnoviji expense', '123', '27/March/2018', '22:12:55', 1, 0),
(9, 'Novi', '12', '27/March/2018', '23:6:43', 1, 0),
(10, 'asd', '123', '27/March/2018', '23:11:31', 1, 0),
(20, 'asd', '123', '27/March/2018', '23:12:32', 1, 0),
(21, 'New', '1', '27/March/2018', '23:12:52', 1, 0),
(22, 'Test', '123', '27/March/2018', '23:32:46', 2, 0),
(23, 'Mateo', '123', '29/March/2018', '15:28:0', 1, 0),
(24, '1', '2', '29/March/2018', '15:28:5', 1, 0),
(25, '1', '2', '29/March/2018', '15:28:30', 1, 0),
(26, '1', '2', '29/March/2018', '15:28:39', 1, 0),
(27, '1', '2', '29/March/2018', '15:28:51', 1, 0),
(28, 'hjkh', '123', '29/March/2018', '16:37:35', 1, 0),
(29, 'gjhg', '122', '29/March/2018', '16:37:59', 1, 0),
(30, 'TestniTrosakObicniUser', '123', '29/March/2018', '16:41:10', 2, 1),
(31, 'Novi', '90', '3/April/2018', '17:8:3', 11, 1),
(32, 'Novi', '120', '3/April/2018', '21:50:45', 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `user_since` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `added_by` int(11) DEFAULT NULL,
  `id` int(11) NOT NULL,
  `admin` tinyint(1) NOT NULL,
  `admin_since` varchar(100) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`, `user_since`, `added_by`, `id`, `admin`, `admin_since`) VALUES
('Mateo', 'Mlinarevic', '1/January/2018', 1, 1, 1, '1/January/2018'),
('Jure', 'Jure', '26/March/2018', 1, 2, 0, ''),
('Mate', 'Mate', '29/March/2018', 1, 4, 0, ''),
('Mario Martic', 'mariomartic', '30/March/2018', 1, 6, 0, ''),
('Ferdo', 'Ferdo', '30/March/2018', 1, 8, 0, ''),
('Grgo', 'grgo', '30/March/2018', 1, 11, 0, ''),
('Matej', 'Matej', '30/March/2018', 1, 12, 1, '30/March/2018');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `balances`
--
ALTER TABLE `balances`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_balance` (`user_id`);

--
-- Indexes for table `expenses`
--
ALTER TABLE `expenses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_expense` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `id_2` (`id`),
  ADD KEY `added_by_user` (`added_by`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `balances`
--
ALTER TABLE `balances`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT for table `expenses`
--
ALTER TABLE `expenses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `balances`
--
ALTER TABLE `balances`
  ADD CONSTRAINT `user_balance` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `expenses`
--
ALTER TABLE `expenses`
  ADD CONSTRAINT `user_expense` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `added_by_user` FOREIGN KEY (`added_by`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
