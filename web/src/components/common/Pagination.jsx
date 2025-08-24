const Pagination = ({ roomsPage, totalRooms, currentPage, paginate }) => {
    const pageNumbers = [];

    for (let i = 1; i < Math.ceil(totalRooms / roomsPage); i++) {
        pageNumbers.push(i);
    }

    return (
        <div className="pagination-nav">
            <ul className="pagination-ul">
                {pageNumbers.map((number) => (
                    <li className="pagination-li" key={number}>
                        <button
                            onClick={() => paginate(number)}
                            className={`pagination-button ${
                                currentPage == number ? "current-page" : ""
                            }`}
                        >
                            {number}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default Pagination;
