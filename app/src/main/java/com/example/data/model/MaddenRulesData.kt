package com.example.data.model

object MaddenRulesData {
    val standardRules = listOf(
        CompetitiveRule(
            title = "4th Down Rules & Punt Protocol",
            category = "Fourth Down",
            ruleText = "You may only go for it on 4th down if you are past the opponent's 50-yard line, OR if you are trailing by 9+ points in the 3rd quarter, OR anytime in the 4th quarter.",
            penalty = "Illegal 4th Down attempt results in immediate turnover and $10 penalty forfeit if challenged."
        ),
        CompetitiveRule(
            title = "Onside Kick Restrictions",
            category = "Special Teams",
            ruleText = "Onside kicks are only permitted when trailing in the 4th quarter or trailing by 21+ points in the 2nd half.",
            penalty = "Illegal onside kick awards ball to opponent at the 45-yard line."
        ),
        CompetitiveRule(
            title = "Disconnect & EA Server Crash Policy",
            category = "Disconnect",
            ruleText = "If a disconnect occurs in Q1 or Q2, rematch must be restarted with the remaining point differential reconstructed. If disconnected in Q4 while trailing by 17+, the trailing player forfeits.",
            penalty = "Rage quit without mutual party agreement results in automatic 0-35 forfeit loss and forfeiture of full wager pot."
        ),
        CompetitiveRule(
            title = "Glitch Plays & Nano-Blitzing",
            category = "General",
            ruleText = "Manual looping of defensive line to exploit blocking AI (unblockable loops without pass rush engagement) is strictly banned. Custom playbooks must not contain banned bug formations.",
            penalty = "Requires video proof submission; confirmed glitching results in match disqualification."
        ),
        CompetitiveRule(
            title = "Chew Clock & Sportsmanship",
            category = "General",
            ruleText = "Chew clock tempo may only be activated inside of 3 minutes remaining in the 2nd quarter or inside of 4 minutes in the 4th quarter.",
            penalty = "Warning on first violation; second violation results in dead-ball delay penalty."
        ),
        CompetitiveRule(
            title = "Final Box Score Proof",
            category = "Verification",
            ruleText = "Both players must capture and keep a photo or video screenshot of the post-game Madden 27 final team stats and player performance screen until the pot payout is settled.",
            penalty = "Failure to provide proof during a disputed match results in default judgment to the opponent."
        )
    )
}
