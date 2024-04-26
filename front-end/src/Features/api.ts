import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { BaseSearchFilter, Movie } from "./type";
import { get } from "http";


export const api = createApi({
  reducerPath: "api",
  baseQuery: fetchBaseQuery({ baseUrl: "http://localhost:8080/movies" }),
  endpoints: (builder) => ({
    getMoviesList: builder.query<Movie[], BaseSearchFilter>({
      query: (filter) => ({
        url: `/search`,
        method: "GET",
        params: filter,
      }),
    }),
    getCountMovies: builder.query<number, BaseSearchFilter>({
      query: (filter) => ({
        url: `/count`,
        method: "GET",
        params: filter,
      }),
    }),
    getGenres: builder.query<string[], void>({
      query: () => `/genres`,
    }),
    getMovieDetail: builder.query<Movie, number>({
      query: (id) => `/${id}`,
    }),
    getRecommededMovies: builder.query<Movie[], number>({
      query: (id) => `${id}/recommend`,
      transformResponse: (response: { movies: Movie }[]) =>
        response.map((movie) => (movie.movies)),
    }),
  }),
});

export const {
  useGetMoviesListQuery,
  useGetGenresQuery,
  useGetCountMoviesQuery,
  useGetMovieDetailQuery,
  useGetRecommededMoviesQuery,
} = api;
