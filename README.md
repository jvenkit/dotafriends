# DotaFriends #

A Dota 2 match tracking app for Android.

### What can it do? ###

Given a valid account ID, the app will download the details of up to the last 500 matches (API limit).

Current features:

* List of matches
* Fetches match/player details in the background
* Basic match details

### How do I use it? ###

In order to build the app you will need a Steam API key from [here](http://steamcommunity.com/dev/apikey). Once you have it, you will need to replace the placeholder string in services/SteamService with your API key.

To start tracking a player, use the 'Track player' option in the action bar. You must enter a valid 32-bit Steam account ID, e.g. `52169507`. Once a player is being tracked, you can update the match list with new matches using the 'Update match list' option.

### Upcoming features ###
* Display win/loss rates with all the people you've played with
* Detailed user stats (averages, records, etc.)
* More detailed match stats (items, towers/barracks, ability upgrades, possibly drafts for Captain's mode/draft
* Prettier interface

### Contact ###
All questions and comments can be sent to

justin.venkit@gmail.com
