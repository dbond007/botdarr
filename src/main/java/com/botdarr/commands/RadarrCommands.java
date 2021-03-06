package com.botdarr.commands;

import com.botdarr.clients.ChatClientResponse;
import com.botdarr.api.RadarrApi;

import java.util.ArrayList;
import java.util.List;

public class RadarrCommands {
  public static List<Command> getCommands(RadarrApi radarrApi) {
    return new ArrayList<Command>() {{
      add(new BaseCommand("movie discover", "Finds new movies based on radarr recommendations (from trakt)") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          return new CommandResponse<>(radarrApi.discover());
        }
      });
      add(new BaseCommand("movie id add", "Adds a movie using search text and tmdb id (i.e., movie id add John Wick 484737). The easiest" +
        " way to use this command is to use \"movie find new TITLE\", then the results will contain the movie add command for you") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          int lastSpace = command.lastIndexOf(" ");
          String searchText = command.substring(0, lastSpace);
          String id = command.substring(lastSpace + 1);
          return new CommandResponse(radarrApi.addWithId(searchText, id));
        }
      });
      add(new BaseCommand("movie title add", "Adds a movie with just a title. Since many movies can have same title or very similar titles, the trakt" +
        " search can return multiple movies, if we detect multiple new films, we will return those films, otherwise we will add the single film.") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          return new CommandResponse(radarrApi.addWithTitle(command));
        }
      });
      add(new BaseCommand("movie profiles", "Displays all the profiles available to search for movies under (i.e., movie title add ANY)") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          return new CommandResponse(radarrApi.getProfiles());
        }
      });
      add(new BaseCommand("movie find new", "Finds a new movie using radarr (i.e., movie find John Wick)") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          return new CommandResponse(radarrApi.lookup(command, true));
        }
      });
      add(new BaseCommand("movie find existing", "Finds an existing movie using radarr (i.e., movie find Princess Fudgecake)") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          return new CommandResponse(radarrApi.lookup(command, false));
        }
      });
      add(new BaseCommand("movie find downloads", "Lists all the available (not rejected) torrents for a movie (i.e., movie find downloads TITLE OF MOVIE). " +
        "You can get the title by using \"movie find existing\". This can be a SLOW operation depending on the number of indexers configured" +
        " in your Radarr settings and particularly how fast each indexer is. Also these are torrents that have not been marked as rejected based" +
        " on whatever quality/profile settings are configured in Radarr") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          return new CommandResponse(radarrApi.lookupTorrents(command, false));
        }
      });
      add(new BaseCommand("movie find all downloads", "List all the available torrents for a movie whether they are rejected by radarr or not") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          return new CommandResponse(radarrApi.lookupTorrents(command, true));
        }
      });
      add(new BaseCommand("movie hash download", "Force downloads a movie using a hash string, you can only get from the command 'movie find all downloads'") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          return new CommandResponse(radarrApi.forceDownload(command));
        }
      });
      add(new BaseCommand("movie downloads", "Shows all the active movies downloading in radarr") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          return new CommandResponse(radarrApi.downloads());
        }
      });
      add(new BaseCommand("movie cancel download", "Cancels a download (NOT IMPLEMENTED YET)") {
        @Override
        public CommandResponse<? extends ChatClientResponse> execute(String command) {
          return new CommandResponse(radarrApi.cancelDownload(command));
        }
      });
    }};
  }
}
