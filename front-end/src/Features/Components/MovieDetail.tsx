import { useNavigate, useParams } from "react-router-dom";
import { useGetMovieDetailQuery, useGetRecommededMoviesQuery } from "../api";
import { Avatar, Button, Card, Col, Layout, Row, Skeleton, Spin } from "antd";
import Meta from "antd/es/card/Meta";
import { LoadingContainer } from "../styled";
import { Content } from "antd/es/layout/layout";
import styled from "@emotion/styled";

export default function MovieDetail() {
    const id = useParams().id;
    const { data: movieDetail, isLoading: isDetailLoading } = useGetMovieDetailQuery(Number(id));
    const { data: recommendedMovies =[], isLoading: isRecommendedLoading, isFetching: isRecommendFetching } = useGetRecommededMoviesQuery(Number(id));
    const navigate = useNavigate();
  if ( isDetailLoading ){
    return (
      <LoadingContainer>
        <Spin size="large" />
      </LoadingContainer>
    )
  }
  console.log(recommendedMovies);
  
  return (
    <div>
      <StyledForm>
        <Button type="primary" onClick={() => navigate("/dashboard")}>Quay lại</Button>
        <Row gutter={16}>
          <div style={{margin: "auto"}}>
            <h1>{movieDetail?.seriesTitle}</h1>
          </div>
        </Row>
        <Row gutter={16}>
          <Col span={8}>
            <img src={movieDetail?.posterLink} alt="poster" style={{width: "100%"}}/>
          </Col>
          <Col span={16}>
            <Row>
              <p>Đạo diễn: {movieDetail?.director}</p>
            </Row>
            <Row>
              <p>Diễn viên: {movieDetail?.star1},{movieDetail?.star2},{movieDetail?.star3},{movieDetail?.star4},...</p>
            </Row>
            <Row>
              <p>Thể loại: {movieDetail?.genre}</p>
            </Row>
            <Row>
              <p>Năm phát hành: {movieDetail?.releasedYear}</p>
            </Row>
            <Row>
              <p>Mô tả: {movieDetail?.overview}</p>
            </Row>
          </Col>
        </Row>
      </StyledForm>
      <Row gutter={16}>
        {isRecommendedLoading || isRecommendFetching ? Array.from(Array(10).keys())
          .map((index) => (
            <Card
              style={{ width: 300, marginTop: 16 }}>
              <Skeleton key={index} active avatar style={{margin:20}}/>
            </Card>
          )): recommendedMovies?.map((movie) => (
            <Card
              hoverable
              style={{ width: 300, marginTop: 16, height: "fit-content"}}
              cover={<img src={movie.posterLink} alt="" style={{width:100}}/>}
              title={movie.seriesTitle}
              onClick={() => navigate(`/movie/${movie.id}`)}
            />              
          ))}
        
      </Row>
    </div>
  );
}

const StyledForm = styled.div`
  margin:10vh 20vw 0 20vw;
  height: fit-content;
`;