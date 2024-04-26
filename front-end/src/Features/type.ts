export interface loginResponse {
  token: string;
}

export interface BaseSearchFilter {
  page?: number;
  limit?: number;
  genre?: string;
  query?: string;
}
export interface RatingState {
  id: number;
  rating: number;
}

export interface Movie {
  id: number;
  seriesTitle?: string;
  releasedYear?: number;
  genre?: string;
  posterLink?: string;
  director?: string;
  star1?: string;
  star2?: string;
  star3?: string;
  star4?: string;
  overview?: string;
}