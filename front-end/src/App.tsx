import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { useSelector } from "react-redux";
import { RootState } from "./Features/store";
import MovieList from "./Features/Components/MovieList";
import MovieDetail from "./Features/Components/MovieDetail";

function App() {
  const state = useSelector((state:RootState) => state.menu);
  
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/dashboard" element={<MovieList />} />
          <Route path="/movie/:id" element={<MovieDetail />} />
          <Route path="*" element={<Navigate to="/dashboard" />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
