package fr.milekat.cite_box.engines;

import fr.milekat.cite_box.MainBox;
import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.utils_tools.DateMilekat;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewDayUpdate {
    private String day;
    private String lastday;

    /**
     *      Check du changement de jour pour changer les coffres du jour !
     */
    public BukkitTask runTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                day = DateMilekat.setLiteDateNow();
                if (!day.equals(lastday) && lastday!=null) {
                    setChestDay(day);
                }
                lastday = day;
            }
        }.runTaskTimerAsynchronously(MainBox.getInstance(), 0L, 600L);
    }

    /**
     *      Mise à jour du coffre du jour sur une date donnée pour tous les joueurs !
     * @param date à appliquer
     */
    public void setChestDay(String date) {
        try {
            Connection connection = MainCore.sql.getConnection();
            PreparedStatement q = connection.prepareStatement("SELECT `box_content` FROM `" + MainCore.SQLPREFIX +
                    "box` WHERE `box_date` = ?;");
            q.setString(1, date);
            q.execute();
            PreparedStatement q2 = connection.prepareStatement("UPDATE `" + MainCore.SQLPREFIX + "player` SET `daily_box` = ?;");
            if (q.getResultSet().last()) {
                q2.setString(1, q.getResultSet().getString("box_content"));
            } else {
                q2.setString(1, ";;;;;;;;;;;;;;;;;;;;;;;;;;");
            }
            q2.execute();
            q2.close();
            q.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        MainCore.sendAnnonce("Coffre du jour mis à jour !");
    }
}
