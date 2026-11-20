package wordguesser.model;

public class User {
    private String username;
    private String password;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private int pvpWins;
    private int pvpLosses;

    public User(String username, String password) {
        this(username, password, 0, 0, 0, 0, 0);
    }

    public User(String username, String password, int gamesPlayed, int wins, int losses, int pvpWins, int pvpLosses) {
        this.username = username;
        this.password = password;
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.losses = losses;
        this.pvpWins = pvpWins;
        this.pvpLosses = pvpLosses;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getPvpWins() {
        return pvpWins;
    }

    public int getPvpLosses() {
        return pvpLosses;
    }

    public void incrementGamesPlayed() {
        gamesPlayed++;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementLosses() {
        losses++;
    }

    public void incrementPvpWins() {
        pvpWins++;
    }

    public void incrementPvpLosses() {
        pvpLosses++;
    }

    public String toFileString() {
        return username + "|" + password + "|" + gamesPlayed + "|" + wins + "|" + losses + "|" + pvpWins + "|" + pvpLosses;
    }

    public static User fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 7) {
            return null;
        }

        try {
            return new User(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4]),
                Integer.parseInt(parts[5]),
                Integer.parseInt(parts[6])
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }
}