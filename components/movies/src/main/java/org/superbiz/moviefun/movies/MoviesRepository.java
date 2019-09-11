/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.superbiz.moviefun.movies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.superbiz.moviefun.movies.api.MovieIntf;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.List;

@Repository
public class MoviesRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    public Movie find(Long id) {
        return entityManager.find(Movie.class, id);
    }

    @Transactional
    public void addMovie(MovieIntf movie) {
        logger.debug("Creating movie with title {}, and year {}", movie.getTitle(), movie.getYear());

        entityManager.persist(movie);
    }

    @Transactional
    public void updateMovie(MovieIntf movie) {
        entityManager.merge(movie);
    }

    @Transactional
    public void deleteMovie(MovieIntf movie) {
        entityManager.remove(movie);
    }

    @Transactional
    public void deleteMovieId(long id) {
        Movie movie = entityManager.find(Movie.class, id);
        deleteMovie(movie);
    }

    public List<MovieIntf> getMovies() {
        CriteriaQuery<MovieIntf> cq = entityManager.getCriteriaBuilder().createQuery(MovieIntf.class);
        cq.select(cq.from(MovieIntf.class));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<MovieIntf> findAll(int firstResult, int maxResults) {
        CriteriaQuery<MovieIntf> cq = entityManager.getCriteriaBuilder().createQuery(MovieIntf.class);
        cq.select(cq.from(MovieIntf.class));
        TypedQuery<MovieIntf> q = entityManager.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public int countAll() {
        CriteriaQuery<Long> cq = entityManager.getCriteriaBuilder().createQuery(Long.class);
        Root<Movie> rt = cq.from(Movie.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        TypedQuery<Long> q = entityManager.createQuery(cq);
        return (q.getSingleResult()).intValue();
    }

    public int count(String field, String searchTerm) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<MovieIntf> root = cq.from(MovieIntf.class);
        EntityType<MovieIntf> type = entityManager.getMetamodel().entity(MovieIntf.class);

        Path<String> path = root.get(type.getDeclaredSingularAttribute(field, String.class));
        Predicate condition = qb.like(path, "%" + searchTerm + "%");

        cq.select(qb.count(root));
        cq.where(condition);

        return entityManager.createQuery(cq).getSingleResult().intValue();
    }

    public List<MovieIntf> findRange(String field, String searchTerm, int firstResult, int maxResults) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MovieIntf> cq = qb.createQuery(MovieIntf.class);
        Root<MovieIntf> root = cq.from(MovieIntf.class);
        EntityType<MovieIntf> type = entityManager.getMetamodel().entity(MovieIntf.class);

        Path<String> path = root.get(type.getDeclaredSingularAttribute(field, String.class));
        Predicate condition = qb.like(path, "%" + searchTerm + "%");

        cq.where(condition);
        TypedQuery<MovieIntf> q = entityManager.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public void clean() {
        entityManager.createQuery("delete from Movie").executeUpdate();
    }
}
