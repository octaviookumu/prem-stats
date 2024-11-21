package com.pl.premier_zone.player;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, ModelMapper modelMapper) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
    }


    public List<PlayerResponse> getPlayers() {
        return playerRepository.findAll().stream()
                .map(player -> modelMapper.map(player, PlayerResponse.class))
                .collect(Collectors.toList());
    }

    public List<PlayerResponse> getPlayersFromTeam(String teamName) {
        return playerRepository.findAll().stream()
                .filter(player -> teamName.equals(player.getTeam()))
                .map(player -> modelMapper.map(player, PlayerResponse.class))
                .collect(Collectors.toList());
    }

    public List<PlayerResponse> getPlayersByName(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getName().toLowerCase().contains(searchText.toLowerCase()))
                .map(player -> modelMapper.map(player, PlayerResponse.class))
                .collect(Collectors.toList());
    }

    public List<PlayerResponse> getPlayersByPosition(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getPos().toLowerCase().contains(searchText.toLowerCase()))
                .map(player -> modelMapper.map(player, PlayerResponse.class))
                .collect(Collectors.toList());
    }

    public List<PlayerResponse> getPlayersByNation(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getNation().toLowerCase().contains(searchText.toLowerCase()))
                .map(player -> modelMapper.map(player, PlayerResponse.class))
                .collect(Collectors.toList());
    }

    public List<PlayerResponse> getPlayersByTeamAndPosition(String team, String position) {
        return playerRepository.findAll().stream()
                .filter(player -> team.equals(player.getTeam()) && position.equals(player.getTeam()))
                .map(player -> modelMapper.map(player, PlayerResponse.class))
                .collect(Collectors.toList());
    }

    public PlayerResponse addPlayer(PlayerRequest playerRequest) {

        // Map the PlayerRequest to a Player entity
        Player player = modelMapper.map(playerRequest, Player.class);

        // Save the player entity to the database
        Player savedPlayer = playerRepository.save(player);

        // Map the saved Player entity to a PlayerResponse
        return modelMapper.map(savedPlayer, PlayerResponse.class);
    }

    public PlayerResponse updatePlayer(PlayerRequest updatedPlayer) {

        Optional<Player> existingPlayer = playerRepository.findByName(updatedPlayer.getName());

        if (existingPlayer.isPresent()) {
            Player playerToUpdate = existingPlayer.get();
            playerToUpdate.setName(updatedPlayer.getName());
            playerToUpdate.setTeam(updatedPlayer.getTeam());
            playerToUpdate.setPos(updatedPlayer.getPos());
            playerToUpdate.setNation(updatedPlayer.getNation());

            playerRepository.save(playerToUpdate);
            return modelMapper.map(playerToUpdate, PlayerResponse.class);
        }

        return null;
    }

    void deletePlayerByName(String playerName) {
        playerRepository.deleteByName(playerName);
    }
}
