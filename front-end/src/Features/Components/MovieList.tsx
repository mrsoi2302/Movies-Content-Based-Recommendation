import { useState } from "react";
import { useGetCountMoviesQuery, useGetGenresQuery, useGetMoviesListQuery } from "../api";
import { AutoComplete, Card, Col, Flex, Input, Pagination, Row, Spin } from "antd";
import { LoadingContainer } from "../styled";
import { useNavigate } from "react-router-dom";

export default function MovieList() {
    const [ query, setQuery ]  = useState("");
    const [genre, setGenre] = useState("");
    const [page, setPage] = useState(0);
    const { data, error, isLoading } = useGetMoviesListQuery({
        query: query,
        genre: genre,
        page: page,
        limit: 10,
    });
    
    const {data: count} = useGetCountMoviesQuery({
        query: query,
        genre: genre,
    });
    const navigate = useNavigate();

    const { data: genres } = useGetGenresQuery();

    return (
        <div>
            <Flex gap={"small"} >
                <Input.Search 
                    style={{margin:"20px 0 0 100px", width:"50vw"}}
                    placeholder="Tìm kiếm bằng tên phim"
                    onChange={(e) => setQuery(e.target.value)}
                    allowClear
                />
                <AutoComplete
                    allowClear
                    onClear={() => {setGenre("")}}
                    placeholder="Chọn thể loại"
                    style={{margin:"20px", width:"20vw"}}
                    options={
                        genres?.map((
                            genre) => 
                                ({ 
                                title: genre,
                                value: genre
                            }))}
                    filterOption={(inputValue, option) =>{
                        return option?.value.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1;
                    }}
                    value={genre}
                    onSelect={(value) => {setGenre(value)}
                }
                />
            </Flex>
            {isLoading ? <LoadingContainer>
                <Spin size="large" />
            </LoadingContainer>:
            <Row gutter={16}>
            {data?.map((movie) => (
                <Col span={4} style={{margin:20}}>
                    <Card 
                        key={movie.id}
                        hoverable
                        title={movie.seriesTitle}
                        style={{ width: 300, height: 400}}
                        cover={<img src={movie.posterLink} alt="" style={{width:100}}/>}
                        onClick={() => navigate(`/movie/${movie.id}`)}
                    >
                        <p>{movie.overview}</p>
                    </Card>   
                </Col> 
            ))}
            </Row>}
            <Pagination 
                pageSize={10}
                total={count} 
                current={page+1}
                onChange={e => setPage(e-1)}
                showSizeChanger={false}
            />
        </div>
    )
}