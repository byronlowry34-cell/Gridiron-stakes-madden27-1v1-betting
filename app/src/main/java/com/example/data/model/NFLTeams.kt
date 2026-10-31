package com.example.data.model

data class NFLTeam(
    val id: String,
    val name: String,
    val city: String,
    val abbreviation: String,
    val overallRating: Int,
    val offRating: Int,
    val defRating: Int,
    val primaryColorHex: Long
)

object NFLTeams {
    val allTeams = listOf(
        NFLTeam("kc", "Chiefs", "Kansas City", "KC", 92, 94, 90, 0xFFE31837),
        NFLTeam("bal", "Ravens", "Baltimore", "BAL", 91, 92, 90, 0xFF241773),
        NFLTeam("det", "Lions", "Detroit", "DET", 90, 93, 87, 0xFF0076B6),
        NFLTeam("sf", "49ers", "San Francisco", "SF", 90, 91, 89, 0xFFAA0000),
        NFLTeam("phi", "Eagles", "Philadelphia", "PHI", 89, 90, 88, 0xFF004C54),
        NFLTeam("buf", "Bills", "Buffalo", "BUF", 89, 92, 86, 0xFF00338D),
        NFLTeam("hou", "Texans", "Houston", "HOU", 88, 89, 87, 0xFF03202F),
        NFLTeam("gb", "Packers", "Green Bay", "GB", 87, 88, 86, 0xFF203731),
        NFLTeam("dal", "Cowboys", "Dallas", "DAL", 87, 89, 85, 0xFF003594),
        NFLTeam("cin", "Bengals", "Cincinnati", "CIN", 86, 91, 82, 0xFFFB4F14),
        NFLTeam("mia", "Dolphins", "Miami", "MIA", 86, 88, 84, 0xFF008E97),
        NFLTeam("min", "Vikings", "Minnesota", "MIN", 85, 87, 84, 0xFF4F2683),
        NFLTeam("atl", "Falcons", "Atlanta", "ATL", 84, 86, 82, 0xFFA71930),
        NFLTeam("lac", "Chargers", "Los Angeles", "LAC", 84, 85, 84, 0xFF0080C6),
        NFLTeam("pit", "Steelers", "Pittsburgh", "PIT", 84, 80, 89, 0xFFFFB612),
        NFLTeam("sea", "Seahawks", "Seattle", "SEA", 83, 84, 83, 0xFF002244),
        NFLTeam("tb", "Buccaneers", "Tampa Bay", "TB", 83, 84, 82, 0xFFD50A0A),
        NFLTeam("lar", "Rams", "Los Angeles", "LAR", 83, 85, 81, 0xFF003594),
        NFLTeam("chi", "Bears", "Chicago", "CHI", 83, 83, 83, 0xFF0B162A),
        NFLTeam("jax", "Jaguars", "Jacksonville", "JAX", 82, 83, 81, 0xFF006778),
        NFLTeam("ind", "Colts", "Indianapolis", "IND", 82, 82, 82, 0xFF002C5F),
        NFLTeam("den", "Broncos", "Denver", "DEN", 81, 80, 83, 0xFFFB4F14),
        NFLTeam("az", "Cardinals", "Arizona", "ARI", 81, 83, 79, 0xFF97233F),
        NFLTeam("nyj", "Jets", "New York", "NYJ", 81, 79, 84, 0xFF125740),
        NFLTeam("no", "Saints", "New Orleans", "NO", 80, 80, 80, 0xFFD3BC8D),
        NFLTeam("cle", "Browns", "Cleveland", "CLE", 80, 78, 84, 0xFF311D00),
        NFLTeam("wsh", "Commanders", "Washington", "WAS", 80, 81, 79, 0xFF5A1414),
        NFLTeam("lv", "Raiders", "Las Vegas", "LV", 79, 79, 80, 0xFFA5ACAF),
        NFLTeam("ten", "Titans", "Tennessee", "TEN", 78, 77, 80, 0xFF0C2340),
        NFLTeam("ne", "Patriots", "New England", "NE", 77, 75, 80, 0xFF002244),
        NFLTeam("nyg", "Giants", "New York", "NYG", 77, 76, 79, 0xFF0B2265),
        NFLTeam("car", "Panthers", "Carolina", "CAR", 76, 76, 77, 0xFF0085CA)
    )

    fun getTeam(abbreviation: String): NFLTeam {
        return allTeams.find { it.abbreviation.equals(abbreviation, ignoreCase = true) }
            ?: allTeams.first()
    }
}
