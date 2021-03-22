-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Erstellungszeit: 22. Mrz 2021 um 08:50
-- Server-Version: 5.7.31
-- PHP-Version: 7.4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `adventure`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `story`
--

DROP TABLE IF EXISTS `story`;
CREATE TABLE IF NOT EXISTS `story` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `storytext` text NOT NULL,
  `img` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf16;

--
-- Daten für Tabelle `story`
--

INSERT INTO `story` (`id`, `storytext`, `img`) VALUES
(1, 'Du hast bei einem Spaziergang neben dem Weg Spitzkegelige KahlkÃ¶pfe gefunden und sie direkt verzehrt du spÃ¼rst schon eine leichte Wirkung. ', 'start.png'),
(2, 'PlÃ¶tzlich stehst du vor einem Hobbit Bau und Gandalf spricht dich an: Komm Auserkorener wir haben dich erwartet. ', 'wo.png'),
(3, 'Du betrittst den Bau der dir von innen etwas grÃ¶sser erscheint. Vor einem tiefen Loch kniet ein weinendes MÃ¤dchen. Peter White spricht zu dir: AuserwÃ¤hlter, Dieses GeschÃ¶pf wurde vom Meister wegen schweren Anschuldigungen zum Tode verurteilt. Vollbringe es! ', 'whob.png'),
(4, 'Peter White: Sehr gut gemacht AuserwÃ¤hlter. Berichte dem Meister dass die Tat vollbracht wurde. ', 'whobk.png'),
(5, 'Peter White: Warum lasst ihr sie gehen? ErklÃ¤rt dies dem Meister selbst! ', 'whobs.png'),
(6, 'Du betrittst die Hallen des Teufels. ErzÃ¼rnt hebt er seine Stimme: Was zumâ€¦ warum wurden meine Befehle nicht ausgefÃ¼hrt. Du trÃ¤gst an allem Schuld AuserwÃ¤hlter. Zur Strafe musst du eine Meiner TÃ¶chter zum Weib nehmen! ', 'whobsch.png'),
(7, 'Du betrittst die Hallen des Teufels. Er hebt seinen Blick und spricht: Na, als Geist wirst du nicht mehr meine Untertanen verfÃ¼hren Muahahahaha. AuserwÃ¤hlter, als dank nimmst du meine Tochter zum Weib. ', 'whobschg.png'),
(8, 'Der gehÃ¶rnte Priester der Unterwelt spricht: AuserwÃ¤hlter, willst du diese Ausgeburt der HÃ¶lle zu deinem Weib nehmen und mit ihr eine Saat des Teufels Ã¼ber die Erde bringen, auf das eure Kinder die Kanalisation verstopfen und die Welt in Exkrementen versinkt? ', 'whoba.png'),
(9, 'Der gehÃ¶rnte Priester der Unterwelt spricht: AuserwÃ¤hlter, willst du diese Ausgeburt der HÃ¶lle zu deinem Weib nehmen und ihr zusehen wie sie andere Menschen vor dir aussaugt, auf das ihr Durst niemals versiegt bis die letzten Tropfen der Menschheit von ihr verschluckt worden sind? ', 'whobv.png'),
(10, 'Wieder vor dem Hobbit Bau. Gandalf lacht: Sehr gut Auserkorener mir gingen schon die Hobbits aus wegen Ihr!  ', 'whobkf.png'),
(11, 'Du bist wieder vor dem Hobbit Bau, du fragst dich mit welcher Art Vampir du zu tun hast.', 'whobsf.png'),
(12, '', ''),
(13, 'Du befindest dich wieder vor dem Hobbit Bau.', 'whobwo.png'),
(14, 'Du kommst an einem See vorbei.', 'woe.png'),
(15, 'WÃ¤hrenddessen in der RealitÃ¤t...', 'woe.gif'),
(16, 'PlÃ¶tzlich kickt der Pilz im Vorhaus.', 'cwfloor.gif'),
(17, 'PlÃ¶tzlich Ã¼berkommt dich ein AngstgefÃ¼hl', 'cwintoh.gif'),
(18, 'Morpheus: Das ist deine letzte Chance. Danach gibt es kein zurÃ¼ck. Nimm die blaue Pille â€” die Geschichte endet, du wachst in deinem Bett auf und glaubst was du auch immer glauben willst. Nimm die rote Pille du bleibst hier im Wunderland und ich werde dir zeigen wie tief das Kaninchenloch reicht.', 'cwchoice.png'),
(19, 'Du wachst in deinem Bett auf und musst dringend zur Toilette.', 'wte.gif'),
(20, 'Morpheus: Ich werde dir jetzt zeigen wie tief das Kaninchenloch reicht. Schnell folge mir.', 'wten.gif'),
(21, 'Montag morgen, es geht dir bestens!', 'end.gif');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `substory`
--

DROP TABLE IF EXISTS `substory`;
CREATE TABLE IF NOT EXISTS `substory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `story_id` int(10) NOT NULL,
  `subtext` text NOT NULL,
  `target_id` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `story_Foreigen_key` (`story_id`),
  KEY `target_Foreign_key` (`target_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf16;

--
-- Daten für Tabelle `substory`
--

INSERT INTO `substory` (`id`, `story_id`, `subtext`, `target_id`) VALUES
(1, 1, 'Du setzt deinen Spaziergang in der Natur fort', 2),
(2, 2, 'Du betrittst den Hobbit Bau ', 3),
(3, 3, 'Du befÃ¶rderst sie mit einem Tritt in die Tiefe', 4),
(4, 3, 'Du hilfst ihr auf und bewahrst sie vor dem sicheren Tod', 5),
(5, 4, 'Du gehst in Richtung Schwefel Geruch ', 7),
(6, 5, 'Du gehst in Richtung Schwefel Geruch ', 6),
(7, 7, 'Hoffentlich redet er nicht von dem Zyklopen TausendfÃ¼ÃŸer  ', 8),
(8, 6, 'Das Zyklopen Fledermaus Insekt sieht nÃ¼tzlich aus', 8),
(9, 6, 'Vampire Diaries Yeeih', 9),
(10, 4, 'Du siehst zu das du wieder aus dem Hobbit Bau rauskommst', 10),
(11, 5, 'Du siehst zu das du mit dem MÃ¤dchen aus dem Hobbit Bau rauskommst', 11),
(12, 9, 'Ja ich will', 11),
(13, 8, 'Ja ich will', 13),
(14, 2, 'Weitergehen und die Natur genieÃŸen', 14),
(15, 14, 'Fischen', 15),
(16, 10, 'Weitergehen und die Natur genieÃŸen', 14),
(17, 11, 'Weitergehen und die Natur genieÃŸen', 14),
(18, 13, 'Weitergehen und die Natur genieÃŸen', 14),
(19, 1, 'Du gehst zurÃ¼ck Nachhause', 16),
(20, 11, 'Du gehst zurÃ¼ck Nachhause', 16),
(21, 10, 'Du gehst zurÃ¼ck Nachhause', 16),
(22, 16, 'Nur noch ein paar Schritte', 17),
(23, 17, 'In welcher Tasche war noch gleich der HaustÃ¼rschlÃ¼ssel', 18),
(24, 18, 'Blaue Pille', 19),
(25, 18, 'Rote Pille', 20),
(26, 20, 'Vielleicht doch lieber schlafen gehen', 21),
(27, 19, 'Schlaf weiter du musst bald zur Arbeit', 21),
(28, 13, 'Du gehst nachhause', 16),
(29, 15, 'ENDE', 1),
(30, 21, 'ENDE', 1);

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `substory`
--
ALTER TABLE `substory`
  ADD CONSTRAINT `story_Foreigen_key` FOREIGN KEY (`story_id`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `target_Foreign_key` FOREIGN KEY (`target_id`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
