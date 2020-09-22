package com.geniusjonathan.goniniteamappserver.repository;

import com.geniusjonathan.goniniteamappserver.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
