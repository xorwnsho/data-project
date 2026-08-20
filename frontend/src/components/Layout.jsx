import { NavLink, Outlet } from "react-router-dom";

const NAV_ITEMS = [
	{ to: "/", label: "AI 상담", end: true },
	{ to: "/explore", label: "상권 탐색기" },
	{ to: "/compare", label: "지역 비교" },
	{ to: "/favorites", label: "관심 지역" },
];

export default function Layout() {
	return (
		<div className="site">
			<header className="site-header">
				<div className="site-header-inner">
					<div className="site-logo">
						<span className="app-badge">AI</span>
						<span className="site-logo-text">대세 상권</span>
					</div>
					<nav className="site-nav">
						{NAV_ITEMS.map((item) => (
							<NavLink
								key={item.to}
								to={item.to}
								end={item.end}
								className={({ isActive }) => `site-nav-link${isActive ? " active" : ""}`}
							>
								{item.label}
							</NavLink>
						))}
					</nav>
				</div>
			</header>

			<div className="site-body">
				<Outlet />
			</div>

			<footer className="site-footer">
				<p>
					본 서비스는 소상공인시장진흥공단 상가(상권)정보 오픈API(data.go.kr)를 활용합니다.
					AI 리포트는 실제 조회된 상권 데이터를 근거로 생성되며, 창업 의사결정의 참고 자료로만 활용해주세요.
				</p>
			</footer>
		</div>
	);
}
